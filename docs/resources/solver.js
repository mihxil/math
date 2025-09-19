/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */
import { BaseClass } from './base.js';

export class SolverClass extends BaseClass {

    constructor() {
        super("#solver", 'org.meeuw.math.demo.Solver');
        this.outcome = this.form.querySelector("#solver_outcome");
        this.input = this.form.querySelector("#solver_input");


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
        const solverResult = await Solver.solve(
            this.model.field, this.outcome.value, this.input.value
        );

        const stream = await solverResult.stream();
        const lines = await stream.toArray();
        for (let i = 0; i < lines.length; i++) {
            this.output.value += "\n" + await lines[i].toString();
        }
        const tries = await (await solverResult.tries()).get();
        const matches = await (await solverResult.matches()).get();
        this.output.value += `\nFound: ${matches}`;
        this.output.value += `\nTried: ${tries}`;
    }
}
//end::solver[]
document.addEventListener("DOMContentLoaded", function() {
    new SolverClass().setup();
});
