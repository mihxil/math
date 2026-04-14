
VERSION=0.19.0
M2_REPO=~/.m2/repository
GROUP_PATH=org/meeuw/math
GROUP_PATH_CONFIG=org/meeuw/configuration
GROUP_PATH_TIME=org/meeuw


help:     ## Show this help.
	@sed -n 's/^##//p' $(MAKEFILE_LIST)
	@grep -E '^[/%a-zA-Z0-9._-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'


.PHONY: act
act:
	act --job build

jars:  ## copy jars
	cp -au $(M2_REPO)/$(GROUP_PATH)/mihxil-math/$(VERSION)/mihxil-math-$(VERSION).jar ./docs/resources/jars/
	cp -au $(M2_REPO)/$(GROUP_PATH_CONFIG)/mihxil-configuration/$(VERSION)/mihxil-configuration-$(VERSION).jar ./docs/resources/jars/
	cp -au $(M2_REPO)/$(GROUP_PATH)/mihxil-math-parser/$(VERSION)/mihxil-math-parser-$(VERSION).jar ./docs/resources/jars/
	cp -au $(M2_REPO)/$(GROUP_PATH)/mihxil-algebra/$(VERSION)/mihxil-algebra-$(VERSION).jar ./docs/resources/jars/
	cp -au $(M2_REPO)/$(GROUP_PATH_TIME)/mihxil-time/$(VERSION)/mihxil-time-$(VERSION).jar ./docs/resources/jars/
	cp -au mihxil-demo/target/original-mihxil-demo-0.20-SNAPSHOT.jar ./docs/resources/jars/

index.html: ./README-source.adoc ./README-source-docinfo.html
	asciidoctor $< -a linkcss -a stylesheet=index.css -a htmled=true -a docs=. -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<
