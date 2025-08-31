/* DO NOT EDIT. Copied from ../mihxil-demo/resources/ */
import { BaseClass } from './base.js';

export class SolverClass extends BaseClass {

    constructor() {
        super("#solver", 'org.meeuw.math.test.Solver');
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
                await this.self.setupForm();
                this.model.field = await this.self.Class.algebraicStructureFor(string, this.input.value)
                this.model.outcome = await Class.parseOutcome(this.field, string);
                this.readyToGo();
                    return await this.outcome.error();
            },
            // this is the 'data-parser' attribute of the 'input' input
            parseInput: async function (string) {
                await this.self.setupForm();
                this.field = await this.self.Class.algebraicStructureFor(this.outcome.value, string);
                this.input = await Class.parseInput(this.field, string);
                this.readyToGo();
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
            this.model.field, self.outcome.value, self.input.value);

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
