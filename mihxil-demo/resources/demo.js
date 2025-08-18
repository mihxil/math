

let cj = null;
async function setupCheerpj() {
    const properties=  [
        `user.timezone=${Intl.DateTimeFormat().resolvedOptions().timeZone}`
    ]
    await cheerpjInit({
        version: 17,
        javaProperties: properties
    });
    const pref = document.location.pathname.startsWith("/math") ? "/app/math/resources/jars/" : "/app/resources/jars/";
    const version = "0.19-SNAPSHOT"
    cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-math-parser-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.14.jar:${pref}big-math-2.3.2.jar`);
}

async function setupFormWithClass(button, ...clazz) {
    button.disabled = true;
    button.textContent = "loading...";
    if (cj === null) {
        await setupCheerpj();
    }
    for (const c of clazz) {
        await cj[c]
    }
    return await cj[clazz[0]];
}

// tag::calculator[]


async function setupCalculator() {

    const form = document.querySelector('#calculator');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const output = form.querySelector('output');
    const input = form.querySelector('input');
    const field = form.querySelector('select');

    function go() {
        button.textContent = buttonText;
        button.disabled = false;
    }

    let Calculator = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        try {
            Calculator = await setupFormWithClass(button, 'org.meeuw.math.test.Calculator');
            output.value = '';
            button.textContent = "executing..";
            console.log("evaluating", input.value, "for", field.value);
            output.value = await Calculator.eval(input.value, field.value);
        } catch (e) {
            let cla = await e.getClass()
            output.value = "" + (await cla.toString()) + ":" + (await e.getMessage());
        }
        go();
    };
}

//end::calculator[]


// tag::solver[]


async function setupSolver() {

    const clazz= 'org.meeuw.math.test.Solver';
    const form = document.querySelector('#solver');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const output = form.querySelector('output');
    const outcome = form.querySelector("#solver_outcome");
    const input = form.querySelector("#solver_input");
    function go() {
        button.textContent = buttonText;
        button.disabled = false;
    }


    const model = {
        field: null,
        outcome: null,
        input: null,
        parseOutcome:  async function(string) {
            Solver = await setupFormWithClass(button, clazz);
            this.field = await Solver.fieldFor(string, input.value)
            this.outcome = await Solver.parseOutcome(this.field, string);
            go();
            return await this.outcome.error();

        },
        parseInput :  async function(string) {
            Solver = await setupFormWithClass(button, clazz);
            this.field = await Solver.algebraicStructureFor(outcome.value, string);
            this.input = await Solver.parseInput(this.field, string);
            go();
            return await this.input.error();
        },
        reset: function() {
            this.outcome =null;
        }
    };
    outcome['model'] = model;
    input['model'] = model;


    let Solver = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        Solver = await setupFormWithClass(button, clazz);

        output.value = '';
        button.textContent = "executing..";
        try {
            output.value += "using: " + await (model.field).toString();
            const solverResult = await Solver.solve(model.field, outcome.value, input.value);

            const stream = await solverResult.stream();
            const lines = await stream.toArray();
            for (let i = 0; i < lines.length; i++) {
                output.value += "\n" + await lines[i].toString();
            }
            const tries = await (await solverResult.tries()).get();
            const matches = await (await solverResult.matches()).get();
            output.value += `\nFound: ${matches}`;
            output.value += `\nTried: ${tries}`;

        } catch (error) {
            output.value += await error.toString();
        }
        go();

    };
}

//end::solver[]




// tag::dynamicdate[]


async function setupDynamicDate() {

    const form = document.querySelector('#dynamicdate');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const output = form.querySelector('output');



    let DynamicDateTime = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        DynamicDateTime = await setupFormWithClass(button, 'org.meeuw.time.parser.DynamicDateTime');
        button.textContent = "executing..";
        try {
            const parser = await new DynamicDateTime();
            const parseResult = await parser.applyWithException(form.querySelector("#dynamicdate_toparse").value);
            output.value = await parseResult.toString();
        } catch (error) {
            output.value = await error.toString();
        }
        button.textContent = buttonText;
        button.disabled = false;
    };
}

//end::dynamicdate[]

async function setupValidation() {
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


document.addEventListener("DOMContentLoaded", function() {
    setupCalculator();
    setupSolver();
    setupDynamicDate();
    setupValidation();
});
