
URL:=mihxil/mihxil-demo:latest
VERSION:=$(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

EXPR:=3 * sin (pi / 4)


help:     ## Show this help.
	@sed -n 's/^##//p' $(MAKEFILE_LIST)
	@grep -E '^[/%a-zA-Z0-9._-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

target/mihxil-demo-$(VERSION).jar:
	(cd ../mihxil-configuration/ ; mvn  install ) ; (cd ../mihxil-math/ ; mvn install ) ; mvn

docker: Dockerfile
	docker build -t $(URL) --build-arg VERSION=$(VERSION) .

localrun: target/math-test-$(VERSION).jar
	java -jar $< prefs


runro:
	docker run -v $(HOME):/root:ro -it  $(URL) prefs

dockerrun:
	docker run -v $(HOME):/root -it  $(URL)

runprefs:
	docker run -v $(HOME):/root -it  $(URL) prefs mm

explore:
	docker run  -$(HOME):/root:ro -it --entrypoint /bin/bash $(URL)

run: index.html  ## run with cheerpj
	npx serve -l 3001

sass/%.css:

resources/css/%.css: sass/%.scss
	sass $< $@



index.html: README-source.adoc README-source-docinfo.html Makefile  resources/*  DEMO-*.adoc  resources/css/demo.css ## create index.html from README-source.adoc
	asciidoctor $< -a htmled=true -a docs=. -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<


test_big:  ## test real
	java -cp target/mihxil-demo-$(VERSION).jar org.meeuw.math.test.Calculator "$(EXPR)" bigdecimal

test_rational:
	java -cp target/mihxil-demo-$(VERSION).jar org.meeuw.math.test.Calculator "$(EXPR)" rational

test_complex:
	java -cp target/mihxil-demo-$(VERSION).jar org.meeuw.math.test.Calculator "$(EXPR)" complex

test_bigcomplex:
	java -cp target/mihxil-demo-$(VERSION).jar org.meeuw.math.test.Calculator "$(EXPR)" bigcomplex


test_real:  ## test real
	java -cp target/mihxil-demo-$(VERSION).jar org.meeuw.math.test.Calculator "$(EXPR)" real

test_gaussian:
	java -cp target/mihxil-demo-$(VERSION).jar org.meeuw.math.test.Calculator "$(EXPR)" gaussian
test: test_big test_rational test_complex test_real
