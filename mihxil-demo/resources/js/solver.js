import { BaseClass, NATIVES } from './base.js';

let instant;

NATIVES['Java_org_meeuw_math_demo_Solver_callBack'] = async function(lib, self, considered, tried, total, expression) {
    instant.button.textContent = `executing.. ${considered}/${total}`;
    //console.log(considered, tried, total, expression);
    return considered;
};


export class SolverClass extends BaseClass {

    constructor() {
        super("#solver", 'org.meeuw.math.demo.Solver');
        this.outcome = this.form.querySelector("#solver_outcome");
        this.input = this.form.querySelector("#solver_input");
        instant = this;

        // model is used by BaseClass#setupValidation.
        this.model = {
            self: this,
            field: null,
            outcome: null,
            input: null,
            // this is the 'data-parser' attribute of the 'outcome' input
            parseOutcome: async function (string) {
                console.log("parsing outcome",  string);
                await this.self.setupForm();
                this.field = await this.self.Class.algebraicStructureFor(string, this.self.input.value)
                this.outcome = await this.self.Class.parseOutcome(this.field, string);
                this.self.readyToGo();
                return await this.outcome.error();
            },
            // this is the 'data-parser' attribute of the 'input' input
            parseInput: async function (string) {
                console.log("parsing input",  string);
                await this.self.setupForm();
                this.field = await this.self.Class.algebraicStructureFor(this.self.outcome.value, string);
                this.input = await this.self.Class.parseInput(this.field, string);
                this.self.readyToGo();
                return await this.input.error();
            },
            reset: function () {
                this.outcome = null;
            }
        };
        this.outcome['model'] = this.model;
        this.input['model'] = this.model;


    }


// tag::solver[]
    async onSubmit(Solver) {
        this.output.value += "using: " + await (this.model.field).toString();
        const solver = await new Solver(this.model.field);
        console.log(solver, this.outcome.value, this.input.value);
        const solverResult = await solver.solve(
            this.outcome.value, this.input.value
        );
        console.log("solverResult", solverResult);
        // using iterator, because I can't figure out java lambda's here.
        const stream = await solverResult.iterator();
        while(await stream.hasNext() && ! this.cancelled) {
            const line = await stream.next();
            this.output.value += "\n" + await line.toString();
            this.output.scrollTop = this.output.scrollHeight;
        }
        const matches = await (await solverResult.matches()).get();
        this.output.value += `\nFound: ${matches}`;
        const tries = await (await solverResult.tries()).get();
        this.output.value += `\nTried: ${tries}`;
        if (this.cancelled) {
            this.output.value += `\nAnd cancelled`;
        }
    }
}
//end::solver[]
document.addEventListener("DOMContentLoaded", function() {
    new SolverClass().setup();
});
