/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */
import { BaseClass } from './base.js';

export class CalculatorClass extends BaseClass {

// tag::calculator[]
    constructor() {
        super(['org.meeuw.math.test.Calculator'], '#calculator');
        this.input = this.form.querySelector('input');
        this.field = this.form.querySelector('select');
        this.inputDataList= this.form.querySelector('datalist');
        this.exampleValues = null;
    }


    async onInView(Calculator){
        await super.onInView(Calculator);
        if (this.exampleValues === null) {
            this.exampleValues = {};
            const fi = await (await BaseClass.cj)['org.meeuw.math.test.Calculator$FieldInformation'];
            const values = await fi.values();

            for (let i = 0; i < values.length; i++) {
                const value = values[i];
                const examples = await value.getExamples();
                const e = [];
                for (let j = 0; j < examples.length; j++) {
                    e[j] = await examples[j];
                }
                this.exampleValues[await values[i].name()] = e;

            }
        }
        this.field.addEventListener('change', () => {
            this.updateDataList();
        });
        this.updateDataList();
    }
    updateDataList() {
        const selectedField = this.field.value;
        const examples = this.exampleValues[selectedField] || [];
        this.inputDataList.innerHTML = '';
        examples.forEach(example => {
            const option = document.createElement('option');
            option.value = example;
            option.text = example;
            this.inputDataList.appendChild(option);
        });
        console.log("Updated data list for", selectedField, examples);

    }

    async onSubmit(Calculator) {
        this.output.value = '';
        this.textContent = "executing..";
        console.log("evaluating", this.input.value, "for", this.field.value);
        this.output.value = await Calculator.eval(
            this.input.value, this.field.value
        );
    }

}
//end::calculator[]

