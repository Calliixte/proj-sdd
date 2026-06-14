# n'étant pas demandé, ni le sujet de l'évaluation mais simplement un outil simplifiant le développement, ce fichier a été généré par IA.
# ── Configuration ────────────────────────────────────────────────────────────
JC        = javac
JVM       = java
JFLAGS    = -g -sourcepath $(SRC_DIR) -d $(BIN_DIR)

SRC_DIR   = src
BIN_DIR   = bin
SOURCES   = $(shell find $(SRC_DIR) -name "*.java")
CLASSES   = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Point d'entrée (ex: com.example.Main ou simplement Main)
MAIN      = Main
TESTS     = Tests.TasTests Tests.AlgorithmsTests Tests.TsplibTests
# ── Cibles ───────────────────────────────────────────────────────────────────
.PHONY: all run clean help

all: $(BIN_DIR) $(CLASSES)
	@echo "✔  Compilation terminée."

## Compile un fichier .java → .class
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JC) $(JFLAGS) $<

## Crée le dossier bin s'il n'existe pas
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

## Lance le programme
run: all
	$(JVM) -cp $(BIN_DIR) $(MAIN)

tests: all
	@for t in $(TESTS); do \
		echo "=== Exécution de $$t ==="; \
		$(JVM) -cp $(BIN_DIR) $$t; \
		echo ""; \
	done
	
## Supprime les fichiers compilés
clean:
	rm -rf $(BIN_DIR)
	@echo "✔  Nettoyage terminé."

## Affiche l'aide
help:
	@echo ""
	@echo "Cibles disponibles :"
	@echo "  make        – compile les sources"
	@echo "  make run    – compile puis lance $(MAIN)"
	@echo "  make tests    – compile puis lance $(TESTS)"
	@echo "  make clean  – supprime le dossier bin"
	@echo ""