all:
	$(MAKE) -C java/
clean:
	$(MAKE) -C java/ clean
test:
	./scripts/mincaml-test-parser.sh
	./scripts/mincaml-test-type-check.sh
	./scripts/mincaml-test-asml.sh
