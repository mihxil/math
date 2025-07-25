

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
    cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.14.jar`);
}

async function setupFormWithClass(button, clazz) {
    button.disabled = true;
    button.textContent = "loading...";
    if (cj === null) {
        await setupCheerpj();
    }
    return await cj[clazz]
}

// tag::solver[]


async function setupSolver() {

    const form = document.querySelector('#solver');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const output = form.querySelector('output');
    const result = form.querySelector("#solver_result");
    const numbers = form.querySelector("#solver_numbers");
    function go() {
        button.textContent = buttonText;
        button.disabled = false;
    }


    const model = {
        ParseResult: null,
        result: null,
        parseIfNeeded: async function() {
          if (this.result == null) {
              await this.parse();
          }
          return this.result;
        },

        parse: async function() {
            const splitNumbers = numbers.value.split(" ");
            if (this.ParseResult == null) {
                this.ParseResult = await setupFormWithClass(button, 'org.meeuw.math.test.Solver$ParseResult');
            }
            this.result = await this.ParseResult.parse(result.value, splitNumbers);
            go();
        },
        parseResult:  async function(input) {
            await this.parse();
            return await this.result.resultError();
        },
        parseInput :  async function(input) {
            await this.parse()
            return await this.result.inputError();
        },
        reset: function() {
            this.result =null;
        }
    };
    result['model'] = model;
    numbers['model'] = model;


    let Solver = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        Solver = await setupFormWithClass(button, 'org.meeuw.math.test.Solver');

        output.value = '';
        button.textContent = "executing..";
        try {
            const parseResult = await model.parseIfNeeded();
            output.value += "using: " + await (await parseResult.field()).toString();
            const solverResult = await Solver.solve(parseResult);

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
        DynamicDateTime = await setupFormWithClass(button, 'org.meeuw.time.dateparser.DynamicDateTime');
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
            model.reset();

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
    setupSolver();
    setupDynamicDate();
    setupValidation();
});
