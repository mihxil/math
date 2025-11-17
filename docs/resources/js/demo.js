/* DO NOT EDIT. Copied from ../mihxil-demo/js/resources/ */
import {BaseClass} from "./base.js";

document.addEventListener("DOMContentLoaded", async function() {
    await document.querySelectorAll(".demo button").forEach(button => {
        button.setAttribute('data-original-text', button.textContent);
        button.textContent = "waiting";
        button.disabled = true;

    });

    const isDemo = document.querySelector('meta[name="demo"]')?.content;
    if (isDemo) {
        console.log("demo page");
        await BaseClass.getCheerpj();
    }
});
