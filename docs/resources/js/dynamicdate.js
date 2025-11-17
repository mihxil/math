/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */
import { BaseClass } from './base.js';

export class DynamicDate extends BaseClass {

    constructor() {
        super("#dynamicdate", 'org.meeuw.time.parser.DynamicDateTime');
        this.form.addEventListener('exampleFilled', async (e) => {
            await this.onSubmit(await this.Class);
        });

    }

// tag::dynamicdate[]

    async onSubmit(DynamicDateTime){
        try {
            const parser = await new DynamicDateTime();
            const parseResult = await parser.applyWithException(
                this.form.querySelector("#dynamicdate_toparse").value
            );
            this.output.value = await parseResult.toString();
        } catch (error) {
            console.log(error);
            this.output.value = await (await error.getMessage()).toString();

        }

    }

//end::dynamicdate[]
}


document.addEventListener("DOMContentLoaded",  function() {
    new DynamicDate().setup();
});

