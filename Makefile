


help:     ## Show this help.
	@sed -n 's/^##//p' $(MAKEFILE_LIST)
	@grep -E '^[/%a-zA-Z0-9._-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'


.PHONY: act
act:
	act --job build

jars:  ## copy jars
	cp -au mihxil-math/target/mihxil-math-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-configuration/target/mihxil-configuration-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-math-parser/target/mihxil-math-parser-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-algebra/target/mihxil-algebra-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-time/target/mihxil-time-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-demo/target/original-mihxil-demo-0.19-SNAPSHOT.jar ./docs/resources/jars/


