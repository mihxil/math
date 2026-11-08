import { BaseClass } from './base.js';

export class CalculatorClass extends BaseClass {

// tag::calculator[]
    constructor() {
        super('#calculator', 'org.meeuw.math.demo.Calculator');
        this.input = this.form.querySelector('input');
        this.field = this.form.querySelector('select');
        this.inputDataList= this.form.querySelector('datalist');
        this.tape = this.form.querySelector('ol.tape');
        this.help = this.form.querySelector('span.help');
        this.information = null;
        this.listenersSetUp = false;
        // true when the input contains the result of the previous calculation (completely selected)
        this.fresh = false;
    }

    get fieldInformation() {
        return this.information[this.field.value];
    }

    /**
     * Inserts the string at the cursor, replacing the selection (if any)
     */
    insert(string) {
        const input = this.input;
        const start = input.selectionStart;
        const end = input.selectionEnd;
        const value = input.value;
        input.value = value.slice(0, start) + string + value.slice(end);
        input.setSelectionRange(
            start + string.length,
            start + string.length
        );
        this.fresh = false;
        input.focus();
    }

    /**
     * Inserts a binary operator. If the input contains a previous result, then the calculation continues with that.
     */
    insertBinaryOperator(operator) {
        if (this.fresh) {
            const input = this.input;
            input.value = input.value + ' ' + operator + ' ';
            input.setSelectionRange(input.value.length, input.value.length);
            this.fresh = false;
            input.focus();
        } else {
            this.insert(operator);
        }
    }

    /**
     * Inserts a unary operator. Single character operators (like '-') are just inserted. Others are functions, which are
     * applied to the selection (e.g. the previous result), or otherwise inserted with the cursor between the brackets.
     */
    insertUnaryOperator(operator) {
        if (operator.length === 1) {
            return this.insert(operator);
        }
        const input = this.input;
        const start = input.selectionStart;
        const end = input.selectionEnd;
        const value = input.value;
        if (start === end) {
            input.value = value.slice(0, start) + operator + "()" + value.slice(end);
            const cursor = start + operator.length + 1;
            input.setSelectionRange(cursor, cursor);
        } else {
            const wrapped = operator + "(" + value.slice(start, end) + ")";
            input.value = value.slice(0, start) + wrapped + value.slice(end);
            input.setSelectionRange(start + wrapped.length, start + wrapped.length);
        }
        this.fresh = false;
        input.focus();
    }

    /**
     * Replaces the complete input (e.g. with an expression from the tape)
     */
    setInput(value) {
        this.input.value = value;
        this.input.setSelectionRange(value.length, value.length);
        this.output.value = '';
        this.fresh = false;
        this.input.focus();
    }

    clear() {
        this.input.value = '';
        this.output.value = '';
        this.output.classList.remove('error');
        this.fresh = false;
        this.input.focus();
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
            if (data === '*' && this.remapStar()) {
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
        this.input.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                e.preventDefault();
                this.clear();
            }
        });
        this.field.addEventListener('change', () => {
            this.updateField();
        });
    }

    async onInView(Calculator){
        await super.onInView(Calculator);
        if (this.information === null) {
            await this.loadInformation();
            this.updateFieldList();
            this.updateField();
        }
    }

    /**
     * Called when the field changes. Everything that depends on the field is updated, and previous results are cleared, since
     * they mean nothing in another field
     */
    updateField() {
        this.updateExamples();
        this.updateHelp();
        this.updateOperators();
        this.updateDigits();
        this.tape.innerHTML = '';
        this.input.value = '';
        this.output.value = '';
        this.output.classList.remove('error');
        this.fresh = false;
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
     * Load all information about the supported fields
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
                    span.onclick = async e => {
                        this.insert(e.target.textContent);
                    };
                    elementsSpans[j] = span;
                }
            }
            this.information[await values[i].name()] = {
                examples: await BaseClass.awaitedArray(value.examples()),
                elements: elements,
                elementsSpans: elementsSpans,
                binaryOperators: await BaseClass.awaitedArray(value.binaryOperators()),
                unaryOperators: await BaseClass.awaitedArray(value.unaryOperators()),
                finite: await value.finite(),
                description: await value.description(),
                help: await value.help(),
                value: value,
            };
        }
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
        const elements = fieldInformation.elementsSpans;
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

    dts(dl, items, onclick, limit=10000) {
        const list = dl.querySelectorAll("dt");

        const currentlyInList = Array.from(list).map(e => e.textContent.trim());
        const unmatchedItems = items.slice(0, limit).filter(op => !currentlyInList.includes(op));

        // add the ones not yet in it.
        unmatchedItems.forEach(op => {
            const dt = document.createElement("dt");
            dt.textContent = op;
            dl.appendChild(dt);
            const dd = document.createElement("dd");
            dl.appendChild(dd);
        });
        let count = 0;
        for (const e of dl.querySelectorAll("dt")) {
            const symbol = e.textContent.trim();
            const title = e.nextElementSibling.textContent;
            if (!e.hasAttribute("original-display")) {
                e.setAttribute("original-display", window.getComputedStyle(e).display);
                e.onclick = onclick;
            }
            if (!items.includes(symbol) || count >= limit) {
                e.style.display = 'none';
                e.nextElementSibling.hidden = true;
            } else {
                e.title = title;
                e.style.display = e.getAttribute("original-display");
                e.nextElementSibling.hidden = false;
                count++;
            }
        }
    }

    updateOperators() {
        const fieldInformation =  this.fieldInformation;
        this.dts(document.querySelector("#calculator_operators dl"), fieldInformation.binaryOperators, e => {
            this.insertBinaryOperator(e.target.textContent.trim());
        });
        this.dts(document.querySelector("#calculator_unary_operator dl"), fieldInformation.unaryOperators, e => {
            this.insertUnaryOperator(e.target.textContent.trim());
        });
    }

    updateDigits() {
        const fieldInformation =  this.fieldInformation;
        // finite structures are small enough to show all elements
        const limit = fieldInformation.finite ? fieldInformation.elements.length : 10;
        this.dts(document.querySelector("#calculator_digits dl"), fieldInformation.elements, e => {
            this.insert(e.target.textContent.trim());
        }, limit);
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

    async onSubmit(Calculator) {
        const expression = this.input.value.trim();
        this.output.classList.remove('error');
        if (expression === '') {
            return;
        }
        const calculator = await new Calculator(this.fieldInformation.value);
        const result = await calculator.calculate(expression);
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

