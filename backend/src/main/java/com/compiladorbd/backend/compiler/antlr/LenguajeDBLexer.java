// $ANTLR 3.5.2 LenguajeDB.g 2026-04-17 10:51:22

package com.compiladorbd.backend.compiler.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class LenguajeDBLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public LenguajeDBLexer() {} 
	public LenguajeDBLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public LenguajeDBLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "LenguajeDB.g"; }

	// $ANTLR start "CREAR_BASE"
	public final void mCREAR_BASE() throws RecognitionException {
		try {
			int _type = CREAR_BASE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:81:12: ( 'crear base' )
			// LenguajeDB.g:81:14: 'crear base'
			{
			match("crear base"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CREAR_BASE"

	// $ANTLR start "TABLA"
	public final void mTABLA() throws RecognitionException {
		try {
			int _type = TABLA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:82:12: ( 'tabla' )
			// LenguajeDB.g:82:14: 'tabla'
			{
			match("tabla"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TABLA"

	// $ANTLR start "CON"
	public final void mCON() throws RecognitionException {
		try {
			int _type = CON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:83:12: ( 'con:' )
			// LenguajeDB.g:83:14: 'con:'
			{
			match("con:"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CON"

	// $ANTLR start "COMO"
	public final void mCOMO() throws RecognitionException {
		try {
			int _type = COMO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:84:12: ( 'como' )
			// LenguajeDB.g:84:14: 'como'
			{
			match("como"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMO"

	// $ANTLR start "TEXTO"
	public final void mTEXTO() throws RecognitionException {
		try {
			int _type = TEXTO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:85:12: ( 'texto' )
			// LenguajeDB.g:85:14: 'texto'
			{
			match("texto"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TEXTO"

	// $ANTLR start "NUMERO"
	public final void mNUMERO() throws RecognitionException {
		try {
			int _type = NUMERO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:86:12: ( 'numero' )
			// LenguajeDB.g:86:14: 'numero'
			{
			match("numero"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMERO"

	// $ANTLR start "FECHA"
	public final void mFECHA() throws RecognitionException {
		try {
			int _type = FECHA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:87:12: ( 'fecha' )
			// LenguajeDB.g:87:14: 'fecha'
			{
			match("fecha"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FECHA"

	// $ANTLR start "PUNTO_COMA"
	public final void mPUNTO_COMA() throws RecognitionException {
		try {
			int _type = PUNTO_COMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:89:12: ( ';' )
			// LenguajeDB.g:89:14: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PUNTO_COMA"

	// $ANTLR start "COMA"
	public final void mCOMA() throws RecognitionException {
		try {
			int _type = COMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:90:12: ( ',' )
			// LenguajeDB.g:90:14: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMA"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:92:12: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// LenguajeDB.g:92:14: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// LenguajeDB.g:92:38: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// LenguajeDB.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// LenguajeDB.g:93:12: ( ( ' ' | '\\n' | '\\t' | '\\r' )+ )
			// LenguajeDB.g:93:14: ( ' ' | '\\n' | '\\t' | '\\r' )+
			{
			// LenguajeDB.g:93:14: ( ' ' | '\\n' | '\\t' | '\\r' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '\t' && LA2_0 <= '\n')||LA2_0=='\r'||LA2_0==' ') ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// LenguajeDB.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			_channel=HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// LenguajeDB.g:1:8: ( CREAR_BASE | TABLA | CON | COMO | TEXTO | NUMERO | FECHA | PUNTO_COMA | COMA | ID | WS )
		int alt3=11;
		switch ( input.LA(1) ) {
		case 'c':
			{
			switch ( input.LA(2) ) {
			case 'r':
				{
				int LA3_9 = input.LA(3);
				if ( (LA3_9=='e') ) {
					int LA3_15 = input.LA(4);
					if ( (LA3_15=='a') ) {
						int LA3_22 = input.LA(5);
						if ( (LA3_22=='r') ) {
							int LA3_29 = input.LA(6);
							if ( (LA3_29==' ') ) {
								alt3=1;
							}

							else {
								alt3=10;
							}

						}

						else {
							alt3=10;
						}

					}

					else {
						alt3=10;
					}

				}

				else {
					alt3=10;
				}

				}
				break;
			case 'o':
				{
				switch ( input.LA(3) ) {
				case 'n':
					{
					int LA3_16 = input.LA(4);
					if ( (LA3_16==':') ) {
						alt3=3;
					}

					else {
						alt3=10;
					}

					}
					break;
				case 'm':
					{
					int LA3_17 = input.LA(4);
					if ( (LA3_17=='o') ) {
						int LA3_24 = input.LA(5);
						if ( ((LA3_24 >= '0' && LA3_24 <= '9')||(LA3_24 >= 'A' && LA3_24 <= 'Z')||LA3_24=='_'||(LA3_24 >= 'a' && LA3_24 <= 'z')) ) {
							alt3=10;
						}

						else {
							alt3=4;
						}

					}

					else {
						alt3=10;
					}

					}
					break;
				default:
					alt3=10;
				}
				}
				break;
			default:
				alt3=10;
			}
			}
			break;
		case 't':
			{
			switch ( input.LA(2) ) {
			case 'a':
				{
				int LA3_11 = input.LA(3);
				if ( (LA3_11=='b') ) {
					int LA3_18 = input.LA(4);
					if ( (LA3_18=='l') ) {
						int LA3_25 = input.LA(5);
						if ( (LA3_25=='a') ) {
							int LA3_31 = input.LA(6);
							if ( ((LA3_31 >= '0' && LA3_31 <= '9')||(LA3_31 >= 'A' && LA3_31 <= 'Z')||LA3_31=='_'||(LA3_31 >= 'a' && LA3_31 <= 'z')) ) {
								alt3=10;
							}

							else {
								alt3=2;
							}

						}

						else {
							alt3=10;
						}

					}

					else {
						alt3=10;
					}

				}

				else {
					alt3=10;
				}

				}
				break;
			case 'e':
				{
				int LA3_12 = input.LA(3);
				if ( (LA3_12=='x') ) {
					int LA3_19 = input.LA(4);
					if ( (LA3_19=='t') ) {
						int LA3_26 = input.LA(5);
						if ( (LA3_26=='o') ) {
							int LA3_32 = input.LA(6);
							if ( ((LA3_32 >= '0' && LA3_32 <= '9')||(LA3_32 >= 'A' && LA3_32 <= 'Z')||LA3_32=='_'||(LA3_32 >= 'a' && LA3_32 <= 'z')) ) {
								alt3=10;
							}

							else {
								alt3=5;
							}

						}

						else {
							alt3=10;
						}

					}

					else {
						alt3=10;
					}

				}

				else {
					alt3=10;
				}

				}
				break;
			default:
				alt3=10;
			}
			}
			break;
		case 'n':
			{
			int LA3_3 = input.LA(2);
			if ( (LA3_3=='u') ) {
				int LA3_13 = input.LA(3);
				if ( (LA3_13=='m') ) {
					int LA3_20 = input.LA(4);
					if ( (LA3_20=='e') ) {
						int LA3_27 = input.LA(5);
						if ( (LA3_27=='r') ) {
							int LA3_33 = input.LA(6);
							if ( (LA3_33=='o') ) {
								int LA3_38 = input.LA(7);
								if ( ((LA3_38 >= '0' && LA3_38 <= '9')||(LA3_38 >= 'A' && LA3_38 <= 'Z')||LA3_38=='_'||(LA3_38 >= 'a' && LA3_38 <= 'z')) ) {
									alt3=10;
								}

								else {
									alt3=6;
								}

							}

							else {
								alt3=10;
							}

						}

						else {
							alt3=10;
						}

					}

					else {
						alt3=10;
					}

				}

				else {
					alt3=10;
				}

			}

			else {
				alt3=10;
			}

			}
			break;
		case 'f':
			{
			int LA3_4 = input.LA(2);
			if ( (LA3_4=='e') ) {
				int LA3_14 = input.LA(3);
				if ( (LA3_14=='c') ) {
					int LA3_21 = input.LA(4);
					if ( (LA3_21=='h') ) {
						int LA3_28 = input.LA(5);
						if ( (LA3_28=='a') ) {
							int LA3_34 = input.LA(6);
							if ( ((LA3_34 >= '0' && LA3_34 <= '9')||(LA3_34 >= 'A' && LA3_34 <= 'Z')||LA3_34=='_'||(LA3_34 >= 'a' && LA3_34 <= 'z')) ) {
								alt3=10;
							}

							else {
								alt3=7;
							}

						}

						else {
							alt3=10;
						}

					}

					else {
						alt3=10;
					}

				}

				else {
					alt3=10;
				}

			}

			else {
				alt3=10;
			}

			}
			break;
		case ';':
			{
			alt3=8;
			}
			break;
		case ',':
			{
			alt3=9;
			}
			break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case '_':
		case 'a':
		case 'b':
		case 'd':
		case 'e':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
			{
			alt3=10;
			}
			break;
		case '\t':
		case '\n':
		case '\r':
		case ' ':
			{
			alt3=11;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 3, 0, input);
			throw nvae;
		}
		switch (alt3) {
			case 1 :
				// LenguajeDB.g:1:10: CREAR_BASE
				{
				mCREAR_BASE(); 

				}
				break;
			case 2 :
				// LenguajeDB.g:1:21: TABLA
				{
				mTABLA(); 

				}
				break;
			case 3 :
				// LenguajeDB.g:1:27: CON
				{
				mCON(); 

				}
				break;
			case 4 :
				// LenguajeDB.g:1:31: COMO
				{
				mCOMO(); 

				}
				break;
			case 5 :
				// LenguajeDB.g:1:36: TEXTO
				{
				mTEXTO(); 

				}
				break;
			case 6 :
				// LenguajeDB.g:1:42: NUMERO
				{
				mNUMERO(); 

				}
				break;
			case 7 :
				// LenguajeDB.g:1:49: FECHA
				{
				mFECHA(); 

				}
				break;
			case 8 :
				// LenguajeDB.g:1:55: PUNTO_COMA
				{
				mPUNTO_COMA(); 

				}
				break;
			case 9 :
				// LenguajeDB.g:1:66: COMA
				{
				mCOMA(); 

				}
				break;
			case 10 :
				// LenguajeDB.g:1:71: ID
				{
				mID(); 

				}
				break;
			case 11 :
				// LenguajeDB.g:1:74: WS
				{
				mWS(); 

				}
				break;

		}
	}



}
