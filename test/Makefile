
URL:=mihxil/math-test:latest

mvn:
	(cd ../mihxil-configuration/ ; mvn  install ) ; (cd ../mihxil-math/ ; mvn install ) ; mvn

docker: Dockerfile
	docker build -t $(URL) .

runro:
	docker run -v $(HOME):/root:ro -it  $(URL)

run:
	docker run -v $(HOME):/root -it  $(URL)

explore:
	docker run  -$(HOME):/root:ro -it --entrypoint /bin/bash $(URL)
