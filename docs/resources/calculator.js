/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */
import { BaseClass } from './base.js';

export class CalculatorClass extends BaseClass {

// tag::calculator[]
    constructor() {
        super('#calculator', 'org.meeuw.math.test.Calculator');
        this.input = this.form.querySelector('input');
        this.field = this.form.querySelector('select');
        this.inputDataList= this.form.querySelector('datalist');
        this.information = null;
    }

    async setupForm() {
        await super.setupForm();
        this.form.addEventListener('beforeinput', async (e) => {
            if (e.data === '=') {
                console.log(this.input.value);
                e.preventDefault();
                e.stopImmediatePropagation();
                await this.handleSubmit();
            }
             if (e.data === '[') {
                 e.preventDefault();
                 e.stopImmediatePropagation();
                 const input = this.input;
                 const start = input.selectionStart;
                 const end = input.selectionEnd;
                 const value = input.value;
                 input.value = value.slice(0, start) + '⋅' + value.slice(end);
                 input.setSelectionRange(start + 1, start + 1);
             }
        });
    }

    async onInView(Calculator){

        await super.onInView(Calculator);
        // using the field information to update the example per field
        if (this.information === null) {
            this.information = {};
            const fi = await (await BaseClass.cj)['org.meeuw.math.test.Calculator$FieldInformation'];
            const values = await fi.values();

            for (let i = 0; i < values.length; i++) {
                const value = values[i];
                const examples = await value.getExamples();
                const description = await value.getDescription();
                const field = await value.getField();
                const string = await field.toString();

                const e = [];
                for (let j = 0; j < examples.length; j++) {
                    e[j] = await examples[j];
                }
                this.information[await values[i].name()] = {
                    examples: e,
                    description: description,
                    string: string
                };
            }
        }
        await this.updateFieldList();
        this.field.addEventListener('change', () => {
            this.updateDataList();
        });
        await this.updateDataList();


    }

    updateFieldList() {
        for (const [key, value] of Object.entries(this.information)) {

            const option = document.createElement('option');
            option.value = key;
            option.text = value.description + ' ' + value.string;
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
                option.text = example;
                this.inputDataList.appendChild(option);
            }
            console.log("Updated data list for", selectedField, information.examples);
        }

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

