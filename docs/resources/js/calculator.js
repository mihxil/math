/* DO NOT EDIT. Copied from ../mihxil-demo/js/resources/ */
import { BaseClass } from './base.js';

/**
 * In templates for unary operators, the place where the operand goes (Calculator.SLOT)
 */
const SLOT = '□';

/**
 * Things that can be used as an operand without brackets
 */
const SIMPLE_OPERAND = /^(-?\d+(\.\d+)?|[\p{L}][\p{L}\p{N}]*|"[^"]*")$/u;

export class CalculatorClass extends BaseClass {

// tag::calculator[]
    constructor() {
        super('#calculator', 'org.meeuw.math.demo.Calculator');
        // the keypad buttons are in the form too, so be explicit about which one submits
        this.button = this.form.querySelector('button[name=submit]');
        this.input = this.form.querySelector('input');
        this.field = this.form.querySelector('select');
        this.inputDataList= this.form.querySelector('datalist');
        this.tape = this.form.querySelector('ol.tape');
        this.help = this.form.querySelector('span.help');
        this.keypad = document.querySelector('#calculator_keypad');
        this.information = null;
        this.calculators = {};
        this.listenersSetUp = false;
        // true when the input contains the result of the previous calculation (completely selected)
        this.fresh = false;
        this.previewTimer = null;
        this.previewSequence = 0;
    }

    get fieldInformation() {
        return this.information[this.field.value];
    }

    /**
     * One Calculator instance per field
     */
    async calculator() {
        const name = this.field.value;
        if (!this.calculators[name]) {
            const Calculator = this.Class;
            this.calculators[name] = await new Calculator(this.fieldInformation.value);
        }
        return this.calculators[name];
    }

    /**
     * Inserts the string at the cursor, replacing the selection (if any)
     */
    insert(string, cursorOffset = string.length) {
        const input = this.input;
        const start = input.selectionStart;
        const end = input.selectionEnd;
        const value = input.value;
        input.value = value.slice(0, start) + string + value.slice(end);
        input.setSelectionRange(start + cursorOffset, start + cursorOffset);
        this.fresh = false;
        input.focus();
        this.schedulePreview();
    }

    /**
     * Inserts a binary operator. If the input contains a previous result, then the calculation continues with that.
     */
    insertBinaryOperator(operator) {
        const input = this.input;
        if (this.fresh) {
            input.setSelectionRange(input.value.length, input.value.length);
        }
        const before = input.value.slice(0, input.selectionStart);
        const padded = (before === '' || before.endsWith(' ') ? '' : ' ') + operator + ' ';
        this.insert(padded);
    }

    /**
     * Applies a unary operator, given as a template like 'sqr(□)', '□!' or '-(□)'. If something is selected (e.g. the previous
     * result), that becomes the operand. Otherwise the template is inserted with the cursor where the operand should go.
     */
    insertUnaryOperator(template) {
        const input = this.input;
        const [before, after] = template.split(SLOT);
        const selected = input.value.slice(input.selectionStart, input.selectionEnd).trim();
        if (selected !== '') {
            const bracketed = template.includes('(' + SLOT + ')');
            const operand = bracketed || SIMPLE_OPERAND.test(selected) ? selected : '(' + selected + ')';
            this.insert(before + operand + after);
        } else {
            // postfix operators (like □!) go after the cursor, others leave the cursor at the operand's place
            this.insert(before + after, before === '' ? after.length : before.length);
        }
    }

    backspace() {
        const input = this.input;
        let start = input.selectionStart;
        const end = input.selectionEnd;
        if (start === end && start > 0) {
            start--;
        }
        input.value = input.value.slice(0, start) + input.value.slice(end);
        input.setSelectionRange(start, start);
        this.fresh = false;
        input.focus();
        this.schedulePreview();
    }

    /**
     * Replaces the complete input (e.g. with an expression from the tape)
     */
    setInput(value) {
        this.input.value = value;
        this.input.setSelectionRange(value.length, value.length);
        this.fresh = false;
        this.input.focus();
        this.schedulePreview();
    }

    clear() {
        this.input.value = '';
        this.clearOutput();
        this.fresh = false;
        this.input.focus();
    }

    clearOutput() {
        this.output.value = '';
        this.output.classList.remove('error', 'preview');
    }

    /**
     * Whether typing '*' should produce '⋅'. Only if this field has no '*' operator of its own (in groups '*' is the group operation)
     */
    remapStar() {
        const operators = this.fieldInformation.binaryOperators;
        return operators.includes('⋅') && !operators.includes('*');
    }

    async setupForm() {
        await super.setupForm();
        if (this.listenersSetUp) {
            // setupForm is called every time the form comes into view
            return;
        }
        this.listenersSetUp = true;
        this.input.addEventListener('beforeinput', async (e) => {
            this.help.textContent = '';
            if (!e.inputType.startsWith('insert') || !e.data || e.data.length !== 1) {
                // deletions, pastes, etc.
                this.fresh = false;
                return;
            }
            let data = e.data;
            if (data === '=') {
                e.preventDefault();
                await this.handleSubmit();
                return;
            }
            if (data === ';') {
                // shortcut for '*', which can be typed without shift
                data = this.fieldInformation.binaryOperators.includes('*') ? '*' : '⋅';
            } else if (data === '*' && this.remapStar()) {
                data = '⋅';
                this.help.textContent = "(* is ⋅ here)";
            }
            if (this.fresh && this.fieldInformation.binaryOperators.includes(data)) {
                // continue with the previous result
                e.preventDefault();
                this.insertBinaryOperator(data);
            } else if (data !== e.data) {
                e.preventDefault();
                this.insert(data);
            } else {
                // just typing, which replaces the previous result if that was there
                this.fresh = false;
            }
        });
        this.input.addEventListener('input', () => {
            this.schedulePreview();
        });
        this.input.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                e.preventDefault();
                this.clear();
            }
        });
        this.field.addEventListener('change', async () => {
            await this.updateField();
        });
    }

    async onInView(Calculator){
        await super.onInView(Calculator);
        if (this.information === null) {
            await this.loadInformation();
            this.updateFieldList();
            await this.updateField();
        }
    }

    /**
     * Called when the field changes. Everything that depends on the field is updated, and previous results are cleared, since
     * they mean nothing in another field
     */
    async updateField() {
        this.updateExamples();
        this.updateHelp();
        this.tape.innerHTML = '';
        this.input.value = '';
        this.clearOutput();
        this.fresh = false;
        await this.updateKeypad();
    }

    updateFieldList() {
        for (const [key, value] of Object.entries(this.information)) {
            const option = document.createElement('option');
            option.value = key;
            option.text = value.description;
            this.field.appendChild(option);
        }
    }

    /**
     * Load all information about the supported fields. The keys of the keypad are loaded when needed (see loadKeys)
     */
    async loadInformation() {
        this.information = {};
        const fi = await (await BaseClass.cj)['org.meeuw.math.demo.Calculator$FieldInformation'];
        const values = await fi.values();

        for (let i = 0; i < values.length; i++) {
            const value = await values[i];
            const elements = await BaseClass.awaitedArray(value.elements());
            let elementsSpans = null;
            if (elements) {
                elementsSpans = [];
                for (let j = 0; j < elements.length; j++) {
                    const span = document.createElement('span');
                    span.classList.add('element');
                    span.textContent = elements[j];
                    elementsSpans[j] = span;
                }
            }
            this.information[await values[i].name()] = {
                examples: await BaseClass.awaitedArray(value.examples()),
                elements: elements,
                elementsSpans: elementsSpans,
                binaryOperators: await BaseClass.awaitedArray(value.binaryOperators()),
                finite: await value.finite(),
                description: await value.description(),
                help: await value.help(),
                keys: null,
                value: value,
            };
        }
    }

    async loadKeys(information) {
        if (information.keys === null) {
            const keys = await BaseClass.awaitedArray(information.value.keys());
            information.keys = [];
            for (const key of keys) {
                information.keys.push({
                    label: await key.label(),
                    insert: await key.insert(),
                    type: await key.type(),
                    title: await key.title(),
                });
            }
        }
        return information.keys;
    }

    updateExamples() {
        this.inputDataList.innerHTML = '';
        for (const example of this.fieldInformation.examples) {
            const option = document.createElement('option');
            option.value = example;
            this.inputDataList.appendChild(option);
        }
    }

    updateHelp() {
        const fieldInformation =  this.fieldInformation;
        const div = this.field.parentNode.querySelector("div.help");
        div.innerHTML = '';
        let help = fieldInformation.help;
        if (help) {
            div.appendChild(document.createTextNode(help));
        }
        // for infinite structures just a sample, the keypad is for entering them
        const elements = fieldInformation.finite ? fieldInformation.elementsSpans : fieldInformation.elementsSpans?.slice(0, 20);
        if (elements && elements.length > 0) {
            div.appendChild(document.createElement("br"));
            div.appendChild(document.createTextNode("elements: "));
            elements.forEach(element => {
                div.appendChild(element);
            })
            if (!fieldInformation.finite) {
                div.appendChild(document.createTextNode("... infinitely many more"));
            }
        }
    }

    keyButton(label, title, onclick, ...classes) {
        const button = document.createElement('button');
        button.type = 'button';
        button.textContent = label;
        if (title) {
            button.title = title;
        }
        button.classList.add(...classes);
        // don't take the focus from the input, so its selection stays
        button.addEventListener('mousedown', e => e.preventDefault());
        button.addEventListener('click', onclick);
        return button;
    }

    /**
     * (Re)builds the on-screen keypad for the current field
     */
    async updateKeypad() {
        if (!this.keypad) {
            return;
        }
        const name = this.field.value;
        const keys = await this.loadKeys(this.fieldInformation);
        if (name !== this.field.value) {
            // field changed again in the meantime
            return;
        }
        const groups = {
            element: this.keypad.querySelector('.elements'),
            binary: this.keypad.querySelector('.binary'),
            unary: this.keypad.querySelector('.unary'),
        };
        Object.values(groups).forEach(g => g.innerHTML = '');
        for (const key of keys) {
            let onclick;
            switch (key.type) {
                case 'binary':
                    onclick = () => this.insertBinaryOperator(key.insert);
                    break;
                case 'unary':
                    onclick = () => this.insertUnaryOperator(key.insert);
                    break;
                default:
                    onclick = () => this.insert(key.insert);
            }
            groups[key.type]?.appendChild(this.keyButton(key.label, key.title, onclick, 'key', key.type));
        }
        const special = this.keypad.querySelector('.special');
        if (special.childElementCount === 0) {
            special.append(
                this.keyButton('(', 'opening bracket', () => this.insert('(')),
                this.keyButton(')', 'closing bracket', () => this.insert(')')),
                this.keyButton('" "', 'quotes, for an element in its own notation, like "1 + 2i"', () => this.insert('""', 1)),
                this.keyButton('⌫', 'backspace', () => this.backspace()),
                this.keyButton('C', 'clear (Esc)', () => this.clear()),
            );
            special.querySelectorAll('button').forEach(b => b.classList.add('key', 'special'));
        }
    }

    /**
     * Evaluates while typing (a bit delayed), and shows the result as a preview
     */
    schedulePreview() {
        clearTimeout(this.previewTimer);
        this.previewTimer = setTimeout(() => this.preview(), 400);
    }

    async preview() {
        const expression = this.input.value.trim();
        const sequence = ++this.previewSequence;
        if (this.running || this.fresh || !this.Class || this.Class === "locked") {
            return;
        }
        if (expression === '') {
            this.clearOutput();
            return;
        }
        const result = await (await this.calculator()).calculate(expression);
        if (sequence !== this.previewSequence || this.running || this.input.value.trim() !== expression) {
            // outdated
            return;
        }
        const error = await result.error();
        this.output.classList.remove('error');
        if (error) {
            // probably just not finished typing
            this.output.classList.remove('preview');
            this.output.value = '';
        } else {
            this.output.classList.add('preview');
            this.output.value = '= ' + await result.result();
        }
    }

    addToTape(expression, result, operand) {
        const li = document.createElement('li');
        const expressionSpan = document.createElement('span');
        expressionSpan.classList.add('expression');
        expressionSpan.textContent = expression;
        expressionSpan.title = 'use this expression again';
        expressionSpan.onclick = () => this.setInput(expression);
        const resultSpan = document.createElement('span');
        resultSpan.classList.add('result');
        resultSpan.textContent = result;
        resultSpan.title = 'use this result';
        resultSpan.onclick = () => this.setInput(operand);
        li.append(expressionSpan, ' = ', resultSpan);
        this.tape.appendChild(li);
        this.tape.scrollTop = this.tape.scrollHeight;
    }

    async onSubmit() {
        clearTimeout(this.previewTimer);
        this.previewSequence++;
        const expression = this.input.value.trim();
        this.clearOutput();
        if (expression === '') {
            return;
        }
        const result = await (await this.calculator()).calculate(expression);
        const error = await result.error();
        if (error) {
            // leave the input, so it can be corrected
            this.output.classList.add('error');
            this.output.value = error;
            this.input.focus();
            return;
        }
        const value = await result.result();
        const operand = await result.operand();
        this.output.value = value;
        this.addToTape(expression, value, operand);
        // the result becomes the start of the next calculation. It is selected, so typing something new replaces it,
        // and typing an operator continues with it
        this.input.value = operand;
        this.input.focus();
        this.input.select();
        this.fresh = true;
    }
}

//end::calculator[]


document.addEventListener("DOMContentLoaded", function() {
    new CalculatorClass().setup();
});

