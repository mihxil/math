


.PHONY: act
act:
	act --job build

jars:
	cp -au mihxil-math/target/mihxil-math-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-algebra/target/mihxil-algebra-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-time/target/mihxil-time-0.19-SNAPSHOT.jar ./docs/resources/jars/
	cp -au mihxil-demo/target/original-mihxil-demo-0.19-SNAPSHOT.jar ./docs/resources/jars/
