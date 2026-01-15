MVN=mvn

.PHONY: resources
.PRECIOUS: sass/%.scss
.DEFAULT_GOAL := index.html

FFMPEG4:=ffmpeg -f png_pipe -i pipe: -s:v 600x600  -pix_fmt yuv420p -vcodec libx264

all: index.html positive-plane.mp4 diagonals-positive-plane.mp4 positive-3-plane.mp4  ## all


# sudo gem install asciidoctor asciidoctor-multipage


help:     ## Show this help.
	@sed -n 's/^##//p' $(MAKEFILE_LIST)
	@grep -E '^[/%a-zA-Z0-9._-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'


index.html: ../README-source.adoc ../README-source-docinfo.html Makefile algebras.svg resources   resources/css/demo.css  resources   resources/css/calculator.css  ## create index.html
	asciidoctor $< -a linkcss -a stylesheet=index.css -a htmled=true -a docs=. -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<

DEMO.html: ../mihxil-demo/README-source.adoc ../mihxil-demo/README-source-docinfo.html resources resources/css/demo-standalone.css  resources/css/demo.css  resources resources/css/calculator.css
	asciidoctor $< -a linkcss -a stylesheet=index.css -a htmled=true -a docs=. -o $@

resources:  ## copy resources from ../mihxil-demo
	find ../mihxil-demo/resources/js -type f -name '*.js' -exec sh -c 'echo "/* DO NOT EDIT. Copied from ../mihxil-demo/js/resources/ */" > resources/js/$$(basename $$1) ; cat $$1 >> resources/js/$$(basename $$1)' sh \{\} \;
	find ../mihxil-demo/resources/images -type f -name '*.svg' -exec sh -c 'echo "<!-- DO NOT EDIT. Copied from ../mihxil-demo/images/resources/ -->" > resources/images/$$(basename $$1) ; cat $$1 >> resources/images/$$(basename $$1)' sh \{\} \;

sass/%.scss: ../mihxil-demo/sass/%.scss
	echo '// DO NOT EDIT' > $@
	cat $< >> $@

resources/css/%.css: sass/%.scss
	sass $< $@

%.svg: %.dot
	dot  -Tsvg $< -o $@

%.dot: %.dot.m4
	m4 $< > $@

%.html: %.adoc %-docinfo.html
	asciidoctor $<


%-3-plane.mp4: %-3-plane.data 3-frame.gp Makefile
	rm -f $@
	for i in {1..999} ; do (echo "\$$data << EOD" ; head -$$i $< ; echo EOD ; echo "set view ,$$(($$i / 3 % 360))"; cat 3-frame.gp) | gnuplot ; done | $(FFMPEG4)   $@

#firefox for file:///Users/michiel/github/mihxil/math/docs/positive-plane.mp4
#Details: auto mozilla::SupportChecker::AddMediaFormatChecker(const TrackInfo &)::(anonymous class)::operator()() const: Decoder may not have the capability to handle the requested video format with YUV444 chroma subsampling.
%-plane.mp4: %-plane.data frame.gp Makefile
	rm -f $@
	for i in {1..999} ; do head -$$i $< | gnuplot frame.gp ; done | $(FFMPEG4)  $@


clean: ## clean
	rm -f algebras.svg algebras.dot index.html


algebras.dot.m4: ../mihxil-algebra/src/test/java/org/meeuw/test/math/abstractalgebra/DocumentationTest.java  ## creates .m4 using mvn
	(cd ../mihxil-algebra ; if `which $(MVN) > /dev/null` ; then $(MVN) -DskipTests=false -Dtest=DocumentationTest test ;fi )

run:  ## Serve locally
	npx serve
