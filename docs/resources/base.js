/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */


export class BaseClass {
    static cj = null;
    static cjInit = null;

    constructor(classes, formSelector, buttonSelector = 'button') {

        const [first, ...rest] = classes;
        this.className = first;
        this.classNames = rest;
        this.form = document.querySelector(formSelector);
        this.button = this.form.querySelector(buttonSelector);
        this.buttonText = this.button.textContent;
        this.output = this.form.querySelector('output');

    }

    static async getCheerpj() {
        if (BaseClass.cjInit === null) {
            BaseClass.cjInit = "locked";
            console.log("init cheerpj 17");
            const properties = [
                `user.timezone=${Intl.DateTimeFormat().resolvedOptions().timeZone}`
            ]
            BaseClass.cjInit = cheerpjInit({
                version: 17,
                javaProperties: properties
            });
        }
        await BaseClass.cjInit;
        if (BaseClass.cj === null) {

            const pref = document.location.pathname.startsWith("/math") ?
                "/app/math/resources/jars/" :
                "/app/resources/jars/";
            const version = "0.19-SNAPSHOT"
            BaseClass.cj = cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-math-parser-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.14.jar:${pref}big-math-2.3.2.jar`);
            console.log("cj -> ", BaseClass.cj);
        }
        return BaseClass.cj;
    }

    static async loadClassesForForm(className, classNames) {
        const cjj = await BaseClass.getCheerpj();

        console.log(cjj, "setting up", className, classNames);
        for (const c of classNames) {
            await cjj[c]
        }
        const clazz = await cjj[className];
        if (!clazz) {
            throw new Error(cjj, "Could not load ", className, classNames);
        }
        return clazz;
    }

    readyToGo() {
        this.button.textContent = this.buttonText;
        this.button.disabled = false;
    }

    async setupForm() {
        this.button.disabled = true;
        this.button.textContent = "loading...";
        if (!this.Class) {
            this.Class = "locked";
            this.Class = await BaseClass.loadClassesForForm(this.className, this.classNames);
            console.log(this.Class, "set up");
        } else if (this.Class === "locked") {
            while (this.Class === "locked") {
                await new Promise(resolve => setTimeout(resolve, 50)); // check every 50ms
            }
        } else {
            console.log(this.Class, "already set up");
        }
        this.form.querySelectorAll("datalist").forEach(dl => {
            dl.addEventListener('click', (e) => {
                const datalist = e.target.closest('datalist').id;
                const optionValue = e.target.value;
                if (optionValue) {
                    document.querySelectorAll(`*[list="${datalist}"]`).forEach(e => {
                        e.value = optionValue;
                    });
                }
            });
        });
        this.form.querySelectorAll("datalist option").forEach(o => {
            if (o.text === '') {
                o.text = o.value;
            }
        });

        await this.readyToGo();
    }

    async onSubmit(Class) {


    }

    async onInView(Class) {
        await this.setupForm();
    }

    async setup(setup) {
        this.form.onsubmit = async (e) => {
            e.preventDefault();
            try {
                await this.setupForm();
                console.log("submitting for", this.Class);
                this.output.value = '';
                this.button.textContent = "executing..";
                await this.onSubmit(this.Class);
            } catch (e) {
                if (e instanceof Promise || e.getMessage) {
                    let error = await e;
                    let cla = await error.getClass()
                    console.log(await cla.toString());
                    this.output.value = await error.getMessage();
                } else {
                    console.log(e);
                    this.output.value = e.stack ? e.stack : e.toString();
                }
            }
            this.readyToGo();
        };
        if (setup) {
            this.setupForm();
        }
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    console.log("intersecting", entry.target, "setup form");
                    this.setupForm()
                    this.onInView(this.Class);
                }
            });
        }, {threshold: 0.1}); // Adjust threshold as needed
        observer.observe(this.form);
    }


    static async setupValidation() {
        document.querySelectorAll(".demo input[pattern]").forEach(input => {

            input.addEventListener("input", function (e) {
                const input = e.target;
                const pattern = new RegExp(input.getAttribute("pattern"));

                if (input.value && !pattern.test(input.value)) {
                    const errorMessage = input.getAttribute("data-error-message") || "Invalid input";

                    input.setCustomValidity(errorMessage);
                    // Optional: add visual feedback
                    input.classList.add("invalid");
                } else {
                    input.setCustomValidity("");
                    input.classList.remove("invalid");
                }
            });
        });
        document.querySelectorAll(".demo input[data-parser]").forEach(input => {

            input.addEventListener("input", async function (e) {
                const input = e.target;
                const parser = input.getAttribute("data-parser");
                const model = input['model'];
                const error = await model[parser](input.value);
                if (error !== null) {
                    const errorMessage = input.getAttribute("data-error-message") || "Invalid input";
                    input.setCustomValidity(errorMessage);
                    // Optional: add visual feedback
                    input.classList.add("invalid");
                    input.reportValidity(); // Show the message immediately
                } else {
                    input.setCustomValidity("");
                    input.classList.remove("invalid");
                }
            });
        });
    }
}


