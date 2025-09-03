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

    insert(c) {
        const input = this.input;
        const start = input.selectionStart;
        const end = input.selectionEnd;
        const value = input.value;
        input.value = value.slice(0, start) + c + value.slice(end);
        input.setSelectionRange(start + 1, start + 1);
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
        // using the field information to update the example per field
        if (this.information === null) {
            this.information = {};
            const fi = await (await BaseClass.cj)['org.meeuw.math.demo.Calculator$FieldInformation'];
            const values = await fi.values();

            for (let i = 0; i < values.length; i++) {
                const value = await values[i];
                const elements = await value.getElements();
                let awaitedElements = null;
                if (elements) {
                    awaitedElements = [];
                    for (let j = 0; j < elements.length; j++) {
                        const span = document.createElement('span');
                        span.classList.add('element');
                        span.textContent = await elements[j];
                        span.onclick = async e => {
                            console.log(e.target.textContent);
                            const input = this.input;
                            const start = input.selectionStart;
                            const end = input.selectionEnd;
                            const value = input.value;
                            const insertText = e.target.textContent;
                            input.value = value.slice(0, start) + insertText + value.slice(end);
                            input.setSelectionRange(start + insertText.length, start + insertText.length);
                            input.focus();
                        };
                        awaitedElements[j] = span;
                    }
                }
                this.information[await values[i].name()] = {
                    examples: await BaseClass.awaitedArray(value.getExamples()),
                    elements: awaitedElements,
                    binaryOperators: await BaseClass.awaitedArray(value.getBinaryOperators()),
                    finite: await value.isFinite(),
                    description: await value.getDescription(),
                    help: await value.getHelp()
                };
            }
            console.log(JSON.stringify(this.information));
        }
        await this.updateFieldList();
        this.field.addEventListener('change', () => {
            this.updateDataList();
            this.updateHelp();
            this.updateOperators();

        });
        await this.updateDataList();
        await this.updateHelp();
        await this.updateOperators();


    }

    updateFieldList() {
        for (const [key, value] of Object.entries(this.information)) {

            const option = document.createElement('option');
            option.value = key;
            option.text = value.description;
            this.field.appendChild(option);
        }
    }

    async updateDataList() {
        const selectedField = this.field.value;
        const information = this.information[selectedField];
        if (information) {
            this.inputDataList.innerHTML = '';
            for (const example of information.examples) {
                const option = document.createElement('option');
                option.value = example;
                this.inputDataList.appendChild(option);
            }
            console.log("Updated data list for", selectedField, information.examples);
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
        const elements = this.information[this.field.value].elements;
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
    async updateOperators() {
        const fieldInformation =  this.information[this.field.value];
        const operators = fieldInformation.binaryOperators;
        const dd = document.querySelector("#calculator_operators");
        dd.querySelectorAll("dt").forEach(e => {
            if (! operators.includes(e.textContent.trim())) {
                e.hidden = true;
                e.nextElementSibling.hidden = true;
            } else {
                e.hidden = false;
                e.nextElementSibling.hidden = false;
            }
        });
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

