

all: index.html

# sudo gem install asciidoctor

index.html: README.adoc
	asciidoctor $< -o $@

%.html: %.adoc
	asciidoctor $<
