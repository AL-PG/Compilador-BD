// $ANTLR 3.5.2 LenguajeDB.g 2026-05-21 01:52:06

package com.compiladorbd.backend.compiler.antlr;

import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.RecognitionException;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class LenguajeDBParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMA", "COMO", "CON", "CREAR_BASE", 
		"FECHA", "ID", "NUMERO", "PUNTO_COMA", "TABLA", "TEXTO", "WS"
	};
	public static final int EOF=-1;
	public static final int COMA=4;
	public static final int COMO=5;
	public static final int CON=6;
	public static final int CREAR_BASE=7;
	public static final int FECHA=8;
	public static final int ID=9;
	public static final int NUMERO=10;
	public static final int PUNTO_COMA=11;
	public static final int TABLA=12;
	public static final int TEXTO=13;
	public static final int WS=14;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public LenguajeDBParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public LenguajeDBParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return LenguajeDBParser.tokenNames; }
	@Override public String getGrammarFileName() { return "LenguajeDB.g"; }


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
	        public boolean esClave;

	        public Atributo(String n, String t) {
	            this.nombre = n;
	            this.tipo = t;
	            this.esClave = false;
	        }

	        public Atributo(String n, String t, boolean clave) {
	            this.nombre = n;
	            this.tipo = t;
	            this.esClave = clave;
	        }
	    }

	    @Override
	    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
	        int line = e.line > 0 ? e.line : 1;
	        String header = getErrorHeader(e);
	        String message = getErrorMessage(e, tokenNames);
	        erroresSintaxis.add(new ErrorSintaxis(line, header + " " + message));
	    }



	// $ANTLR start "script"
	// LenguajeDB.g:66:1: script : declaracion_base ( declaracion_tabla )+ EOF ;
	public final void script() throws RecognitionException {
		try {
			// LenguajeDB.g:66:8: ( declaracion_base ( declaracion_tabla )+ EOF )
			// LenguajeDB.g:66:10: declaracion_base ( declaracion_tabla )+ EOF
			{
			pushFollow(FOLLOW_declaracion_base_in_script31);
			declaracion_base();
			state._fsp--;

			// LenguajeDB.g:66:27: ( declaracion_tabla )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==TABLA) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// LenguajeDB.g:66:27: declaracion_tabla
					{
					pushFollow(FOLLOW_declaracion_tabla_in_script33);
					declaracion_tabla();
					state._fsp--;

					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			match(input,EOF,FOLLOW_EOF_in_script36); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "script"



	// $ANTLR start "declaracion_base"
	// LenguajeDB.g:68:1: declaracion_base : CREAR_BASE ID PUNTO_COMA ;
	public final void declaracion_base() throws RecognitionException {
		Token ID1=null;

		try {
			// LenguajeDB.g:68:18: ( CREAR_BASE ID PUNTO_COMA )
			// LenguajeDB.g:68:20: CREAR_BASE ID PUNTO_COMA
			{
			match(input,CREAR_BASE,FOLLOW_CREAR_BASE_in_declaracion_base45); 
			ID1=(Token)match(input,ID,FOLLOW_ID_in_declaracion_base47); 
			match(input,PUNTO_COMA,FOLLOW_PUNTO_COMA_in_declaracion_base49); 
			 nombreBaseDatos = (ID1!=null?ID1.getText():null); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "declaracion_base"



	// $ANTLR start "declaracion_tabla"
	// LenguajeDB.g:72:1: declaracion_tabla : TABLA ID CON campo ( COMA campo )* PUNTO_COMA ;
	public final void declaracion_tabla() throws RecognitionException {
		Token ID2=null;

		try {
			// LenguajeDB.g:72:19: ( TABLA ID CON campo ( COMA campo )* PUNTO_COMA )
			// LenguajeDB.g:72:21: TABLA ID CON campo ( COMA campo )* PUNTO_COMA
			{
			match(input,TABLA,FOLLOW_TABLA_in_declaracion_tabla96); 
			ID2=(Token)match(input,ID,FOLLOW_ID_in_declaracion_tabla98); 
			match(input,CON,FOLLOW_CON_in_declaracion_tabla100); 

			                       Tabla t = new Tabla((ID2!=null?ID2.getText():null));
			                       tablas.add(t);
			                    
			pushFollow(FOLLOW_campo_in_declaracion_tabla144);
			campo();
			state._fsp--;

			// LenguajeDB.g:77:27: ( COMA campo )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==COMA) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// LenguajeDB.g:77:28: COMA campo
					{
					match(input,COMA,FOLLOW_COMA_in_declaracion_tabla147); 
					pushFollow(FOLLOW_campo_in_declaracion_tabla149);
					campo();
					state._fsp--;

					}
					break;

				default :
					break loop2;
				}
			}

			match(input,PUNTO_COMA,FOLLOW_PUNTO_COMA_in_declaracion_tabla153); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "declaracion_tabla"



	// $ANTLR start "campo"
	// LenguajeDB.g:80:1: campo : id1= ID COMO tipo (id2= ID )? ;
	public final void campo() throws RecognitionException {
		Token id1=null;
		Token id2=null;
		ParserRuleReturnScope tipo3 =null;

		try {
			// LenguajeDB.g:80:7: (id1= ID COMO tipo (id2= ID )? )
			// LenguajeDB.g:80:9: id1= ID COMO tipo (id2= ID )?
			{
			id1=(Token)match(input,ID,FOLLOW_ID_in_campo182); 
			match(input,COMO,FOLLOW_COMO_in_campo184); 
			pushFollow(FOLLOW_tipo_in_campo186);
			tipo3=tipo();
			state._fsp--;

			// LenguajeDB.g:80:26: (id2= ID )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==ID) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// LenguajeDB.g:80:27: id2= ID
					{
					id2=(Token)match(input,ID,FOLLOW_ID_in_campo191); 
					}
					break;

			}


			           boolean clave = (id2 != null && "clave".equals((id2!=null?id2.getText():null)));
			           Tabla tActual = tablas.get(tablas.size() - 1);
			           tActual.atributos.add(new Atributo((id1!=null?id1.getText():null), (tipo3!=null?input.toString(tipo3.start,tipo3.stop):null), clave));
			        
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "campo"


	public static class tipo_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "tipo"
	// LenguajeDB.g:88:1: tipo : ( TEXTO | NUMERO | FECHA );
	public final LenguajeDBParser.tipo_return tipo() throws RecognitionException {
		LenguajeDBParser.tipo_return retval = new LenguajeDBParser.tipo_return();
		retval.start = input.LT(1);

		try {
			// LenguajeDB.g:88:6: ( TEXTO | NUMERO | FECHA )
			// LenguajeDB.g:
			{
			if ( input.LA(1)==FECHA||input.LA(1)==NUMERO||input.LA(1)==TEXTO ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "tipo"

	// Delegated rules



	public static final BitSet FOLLOW_declaracion_base_in_script31 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_declaracion_tabla_in_script33 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_EOF_in_script36 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CREAR_BASE_in_declaracion_base45 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ID_in_declaracion_base47 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_PUNTO_COMA_in_declaracion_base49 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TABLA_in_declaracion_tabla96 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ID_in_declaracion_tabla98 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_CON_in_declaracion_tabla100 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_campo_in_declaracion_tabla144 = new BitSet(new long[]{0x0000000000000810L});
	public static final BitSet FOLLOW_COMA_in_declaracion_tabla147 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_campo_in_declaracion_tabla149 = new BitSet(new long[]{0x0000000000000810L});
	public static final BitSet FOLLOW_PUNTO_COMA_in_declaracion_tabla153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_campo182 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_COMO_in_campo184 = new BitSet(new long[]{0x0000000000002500L});
	public static final BitSet FOLLOW_tipo_in_campo186 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_ID_in_campo191 = new BitSet(new long[]{0x0000000000000002L});
}
