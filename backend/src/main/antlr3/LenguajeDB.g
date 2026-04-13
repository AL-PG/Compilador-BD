grammar LenguajeDB;

@header {
package com.compiladorbd.backend.compiler.antlr;

import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.RecognitionException;
}

@lexer::header {
package com.compiladorbd.backend.compiler.antlr;
}

@members {
    public String nombreBaseDatos = "";
    public List<Tabla> tablas = new ArrayList<Tabla>();
    public List<ErrorSintaxis> erroresSintaxis = new ArrayList<ErrorSintaxis>();

    public class ErrorSintaxis {
        public int line;
        public String message;

        public ErrorSintaxis(int line, String message) {
            this.line = line;
            this.message = message;
        }
    }

    public class Tabla {
        public String nombre;
        public List<Atributo> atributos = new ArrayList<Atributo>();

        public Tabla(String n) {
            this.nombre = n;
        }
    }

    public class Atributo {
        public String nombre;
        public String tipo;

        public Atributo(String n, String t) {
            this.nombre = n;
            this.tipo = t;
        }
    }

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        int line = e.line > 0 ? e.line : 1;
        String header = getErrorHeader(e);
        String message = getErrorMessage(e, tokenNames);
        erroresSintaxis.add(new ErrorSintaxis(line, header + " " + message));
    }
}

script : declaracion_base declaracion_tabla+ EOF ;

declaracion_base : CREAR_BASE ID PUNTO_COMA
                   { nombreBaseDatos = $ID.text; }
                 ;

declaracion_tabla : TABLA ID CON
                    {
                       Tabla t = new Tabla($ID.text);
                       tablas.add(t);
                    }
                    campo (COMA campo)* PUNTO_COMA
                  ;

campo : ID COMO tipo
        {
           Tabla tActual = tablas.get(tablas.size() - 1);
           tActual.atributos.add(new Atributo($ID.text, $tipo.text));
        }
      ;

tipo : TEXTO | NUMERO | FECHA ;

CREAR_BASE : 'crear base' ;
TABLA      : 'tabla' ;
CON        : 'con:' ;
COMO       : 'como' ;
TEXTO      : 'texto' ;
NUMERO     : 'numero' ;
FECHA      : 'fecha' ;

PUNTO_COMA : ';' ;
COMA       : ',' ;

ID         : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;
WS         : (' ' | '\n' | '\t' | '\r')+ {$channel=HIDDEN; } ;
