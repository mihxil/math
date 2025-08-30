
import {CalculatorClass} from './calculator.js';
import {ResolverClass} from './resolver.js';
import {DynamicDate} from './dynamicdate.js';
import {BaseClass} from "./base.js";


document.addEventListener("DOMContentLoaded", async function() {
    const isDemo = document.querySelector('meta[name="demo"]')?.content;
    if (isDemo) {
        console.log("demo page");
        await BaseClass.getCheerpj();
    }

    new CalculatorClass().setup(false);
    new ResolverClass().setup(false);
    new DynamicDate().setup(false);
    BaseClass.setupValidation();
});
