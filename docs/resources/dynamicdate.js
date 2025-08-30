/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */

import { BaseClass } from './base.js';
export class DynamicDate extends BaseClass {

    constructor() {
        super(['org.meeuw.time.parser.DynamicDateTime'], '#dynamicdate');

    }

// tag::dynamicdate[]

    async onSubmit(DynamicDateTime){

        const parser = await new DynamicDateTime();
        const parseResult = await parser.applyWithException(this.form.querySelector("#dynamicdate_toparse").value);
        this.output.value = await parseResult.toString();

    }

//end::dynamicdate[]
}

