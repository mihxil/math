import {CalculatorClass} from './calculator.js';
import {SolverClass} from './solver.js';
import {DynamicDate} from './dynamicdate.js';
import {BaseClass} from "./base.js";

document.querySelectorAll(".demo button").forEach(button => {
    button.disabled = true;
    button.setAttribute('data-original-text', button.textContent);
    button.textContent = "waiting";
});


document.addEventListener("DOMContentLoaded", async function() {
    const isDemo = document.querySelector('meta[name="demo"]')?.content;
    if (isDemo) {
        console.log("demo page");
        await BaseClass.getCheerpj();
    }
    new CalculatorClass().setup();
    new SolverClass().setup();
    new DynamicDate().setup();
});
