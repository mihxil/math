/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */
import { BaseClass } from './base.js';

export class CalculatorClass extends BaseClass {

// tag::calculator[]
    constructor() {
        super('#calculator', 'org.meeuw.math.demo.Calculator');
        this.input = this.form.querySelector('input');
        this.field = this.form.querySelector('select');
        this.inputDataList= this.form.querySelector('datalist');
        this.information = null;
    }

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
        input.focus();
    }

    insertOperator(string) {
        const needsBrackets = string.length > 1;
        if (! needsBrackets) {
            return this.insert(string);
        }
        const input = this.input;
        const start = input.selectionStart;
        const end = input.selectionEnd;
        const value = input.value;
        if (start === end) {
            input.value = string + "(" + value + ")";
            input.setSelectionRange(
                start + string.length + 1,
                start + string.length + 1
            );
        } else {
            input.value = value.slice(0, start) + string + "(" + value.slice(start, end) + ")" + value.slice(end);
            input.setSelectionRange(
                start,
                end  + string.length + 2
            );
        }

        input.focus();
    }

    async setupForm() {
        await super.setupForm();
        this.form.addEventListener('beforeinput', async (e) => {
            this.form.querySelector("span.help").innerHTML = '';
            if (e.data === '=') {
                console.log(this.input.value);
                e.preventDefault();
                e.stopImmediatePropagation();
                await this.handleSubmit();
            }
             if (e.data === '*') {
                 this.form.querySelector("span.help").innerHTML = "to type * use ;";
                 e.preventDefault();
                 e.stopImmediatePropagation();
                 this.insert('⋅')
             }
            if (e.data === ';') {
                 e.preventDefault();
                 e.stopImmediatePropagation();
                 this.insert('*')
             }
        });
    }

    async onInView(Calculator){
        await super.onInView(Calculator);
        if (this.information === null) {
            await this.loadInformation();
            await this.updateFieldList();
            await this.updateExamples();
            await this.updateHelp();
            await this.updateOperators();
            await this.updateDigits();
        }

        this.field.addEventListener('change', () => {
            this.updateExamples();
            this.updateHelp();
            this.updateOperators();
            this.updateDigits();

        });



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
            const elements = await BaseClass.awaitedArray(value.getElements());
            let elementSpans = null;
            if (elements) {
                elementSpans = [];
                for (let j = 0; j < elements.length; j++) {
                    const span = document.createElement('span');
                    span.classList.add('element');
                    span.textContent = elements[j];
                    span.onclick = async e => {
                        this.insert(e.target.textContent);
                    };
                    elementSpans[j] = span;
                }
            }
            this.information[await values[i].name()] = {
                examples: await BaseClass.awaitedArray(value.getExamples()),
                elements: elements,
                elementsSpans: elementSpans,
                binaryOperators: await BaseClass.awaitedArray(value.getBinaryOperators()),
                unaryOperators: await BaseClass.awaitedArray(value.getUnaryOperators()),
                finite: await value.isFinite(),
                description: await value.getDescription(),
                help: await value.getHelp()
            };
        }
        console.log("information", JSON.stringify(this.information,  (key, value) => {
            if (key === 'elementsSpans') {
                return undefined;
            }
            return value;
        } , 1));
    }

    /**
     *
     */
    async updateExamples() {
        const selectedField = this.field.value;
        const information = this.information[selectedField];
        if (information) {
            this.inputDataList.innerHTML = '';
            for (const example of information.examples) {
                const option = document.createElement('option');
                option.value = example;
                this.inputDataList.appendChild(option);
            }
            console.log("Updated example li for", selectedField, information.examples);
        }
    }
    async updateHelp() {
        const fieldInformation =  this.information[this.field.value];
        const div = this.field.parentNode.querySelector("div.help");
        div.innerHTML = '';
        let help = fieldInformation.help;
        if (help) {
            div.appendChild(document.createTextNode(help));
        }
        const elements = this.information[this.field.value].elementSpans;
        if (elements) {
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
    operatorDts(dl, operators) {
        this.dts(dl, operators, async e => {
            this.insertOperator(e.target.textContent);
        });
    }


    dts(dl, items, onclick, limit=10000) {
        const list = dl.querySelectorAll("dt");

        const currentlyInList = Array.from(list).map(e => e.textContent.trim());
        const unmatchedItems = items.slice(0, limit).filter(op => !currentlyInList.includes(op));

        // add the ones not yet in it.
        unmatchedItems.forEach(op => {
            const dt = document.createElement("dt");
            //dt.classList.add('hdlist1');
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
                e.onclick =onclick;
            }
            if (!items.includes(symbol)) {
                e.style.display = 'none';
                e.nextElementSibling.hidden = true;
            } else {
                e.title = title;
                e.style.display = e.getAttribute("original-display");
                e.nextElementSibling.hidden = false;
                count++;
                if (count >= limit) {
                    break;
                }
            }
        }
    }

    async updateOperators() {
        const fieldInformation =  this.information[this.field.value];
        const operators = fieldInformation.binaryOperators;
        this.operatorDts(document.querySelector("#calculator_operators dl"), operators);
        const unaryOperators = fieldInformation.unaryOperators;
        this.operatorDts(document.querySelector("#calculator_unary_operator dl"), unaryOperators);
    }


    async updateDigits() {
        const fieldInformation =  this.information[this.field.value];
        const elements = fieldInformation.elements;
        this.dts(document.querySelector("#calculator_digits dl"), elements, async e => {
            this.insert(e.target.textContent);
        }, 10);

    }

    async onSubmit(Calculator) {
        this.output.value = '';
        this.textContent = "executing..";
        //console.log("evaluating", this.input.value, "for", this.field.value);
        this.output.value = await Calculator.eval(
            this.input.value, this.field.value
        );
    }

}
//end::calculator[]


document.addEventListener("DOMContentLoaded", function() {
    new CalculatorClass().setup();
});

