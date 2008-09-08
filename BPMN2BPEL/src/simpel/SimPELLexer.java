// $ANTLR 3.1b1 SimPEL.g 2008-06-07 13:59:50

package simpel;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SimPELLexer extends Lexer {
    public static final int CR=41;
    public static final int ONALARM=29;
    public static final int CORR_MAP=19;
    public static final int T__57=57;
    public static final int T__51=51;
    public static final int EXPR=22;
    public static final int COMPENSATION=16;
    public static final int PARTNERLINK=20;
    public static final int T__69=69;
    public static final int T__47=47;
    public static final int T__73=73;
    public static final int NAMESPACE=26;
    public static final int T__50=50;
    public static final int CLIENT=7;
    public static final int T__65=65;
    public static final int CORRELATION=18;
    public static final int T__72=72;
    public static final int T__70=70;
    public static final int T__67=67;
    public static final int T__74=74;
    public static final int PARTNER=6;
    public static final int VAR_MODS=36;
    public static final int T__52=52;
    public static final int T__46=46;
    public static final int T__68=68;
    public static final int REPLY=10;
    public static final int T__62=62;
    public static final int SL_COMMENTS=42;
    public static final int INT=37;
    public static final int RECEIVE=9;
    public static final int ASSIGN=11;
    public static final int XML_LITERAL=24;
    public static final int T__49=49;
    public static final int T__61=61;
    public static final int T__59=59;
    public static final int DIGIT=39;
    public static final int T__54=54;
    public static final int T__48=48;
    public static final int EXIT=14;
    public static final int FOREXP=31;
    public static final int T__56=56;
    public static final int ID=33;
    public static final int LETTER=38;
    public static final int T__78=78;
    public static final int WS=43;
    public static final int T__58=58;
    public static final int STRING=35;
    public static final int T__64=64;
    public static final int T__44=44;
    public static final int T__66=66;
    public static final int ONMESSAGE=30;
    public static final int INVOKE=8;
    public static final int COMPENSATE=17;
    public static final int T__77=77;
    public static final int UNTILEXP=32;
    public static final int NS=27;
    public static final int T__45=45;
    public static final int T__55=55;
    public static final int SEMI=34;
    public static final int ROOT=4;
    public static final int PROCESS=5;
    public static final int T__63=63;
    public static final int T__75=75;
    public static final int WAIT=13;
    public static final int ALARM=15;
    public static final int VARIABLE=21;
    public static final int EOF=-1;
    public static final int T__53=53;
    public static final int EXT_EXPR=23;
    public static final int ESCAPE_SEQ=40;
    public static final int CALL=25;
    public static final int T__76=76;
    public static final int T__60=60;
    public static final int T__71=71;
    public static final int THROW=12;
    public static final int PATH=28;

    // delegates
    // delegators

    public SimPELLexer() {;} 
    public SimPELLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SimPELLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "SimPEL.g"; }

    // $ANTLR start T__44
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:11:7: ( 'process' )
            // SimPEL.g:11:9: 'process'
            {
            match("process"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__44

    // $ANTLR start T__45
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:12:7: ( '{' )
            // SimPEL.g:12:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__45

    // $ANTLR start T__46
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:13:7: ( '}' )
            // SimPEL.g:13:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__46

    // $ANTLR start T__47
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:14:7: ( 'partner' )
            // SimPEL.g:14:9: 'partner'
            {
            match("partner"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__47

    // $ANTLR start T__48
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:15:7: ( '=' )
            // SimPEL.g:15:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__48

    // $ANTLR start T__49
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:16:7: ( '(' )
            // SimPEL.g:16:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__49

    // $ANTLR start T__50
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:17:7: ( '@' )
            // SimPEL.g:17:9: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__50

    // $ANTLR start T__51
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:18:7: ( ')' )
            // SimPEL.g:18:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__51

    // $ANTLR start T__52
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:19:7: ( 'client' )
            // SimPEL.g:19:9: 'client'
            {
            match("client"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__52

    // $ANTLR start T__53
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:20:7: ( 'invoke' )
            // SimPEL.g:20:9: 'invoke'
            {
            match("invoke"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__53

    // $ANTLR start T__54
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:21:7: ( ',' )
            // SimPEL.g:21:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__54

    // $ANTLR start T__55
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:22:7: ( '#' )
            // SimPEL.g:22:9: '#'
            {
            match('#'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__55

    // $ANTLR start T__56
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:23:7: ( 'receive' )
            // SimPEL.g:23:9: 'receive'
            {
            match("receive"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__56

    // $ANTLR start T__57
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:24:7: ( 'reply' )
            // SimPEL.g:24:9: 'reply'
            {
            match("reply"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__57

    // $ANTLR start T__58
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:25:7: ( 'onMessage' )
            // SimPEL.g:25:9: 'onMessage'
            {
            match("onMessage"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__58

    // $ANTLR start T__59
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:26:7: ( 'onAlarm' )
            // SimPEL.g:26:9: 'onAlarm'
            {
            match("onAlarm"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__59

    // $ANTLR start T__60
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:27:7: ( 'for' )
            // SimPEL.g:27:9: 'for'
            {
            match("for"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__60

    // $ANTLR start T__61
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:28:7: ( 'until' )
            // SimPEL.g:28:9: 'until'
            {
            match("until"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__61

    // $ANTLR start T__62
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:29:7: ( 'throw' )
            // SimPEL.g:29:9: 'throw'
            {
            match("throw"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__62

    // $ANTLR start T__63
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:30:7: ( 'exit' )
            // SimPEL.g:30:9: 'exit'
            {
            match("exit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__63

    // $ANTLR start T__64
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:31:7: ( 'namespace' )
            // SimPEL.g:31:9: 'namespace'
            {
            match("namespace"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__64

    // $ANTLR start T__65
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:32:7: ( 'var' )
            // SimPEL.g:32:9: 'var'
            {
            match("var"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__65

    // $ANTLR start T__66
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:33:7: ( '|' )
            // SimPEL.g:33:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__66

    // $ANTLR start T__67
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:34:7: ( ':' )
            // SimPEL.g:34:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__67

    // $ANTLR start T__68
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:35:7: ( '==' )
            // SimPEL.g:35:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__68

    // $ANTLR start T__69
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:36:7: ( '!=' )
            // SimPEL.g:36:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__69

    // $ANTLR start T__70
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:37:7: ( '<' )
            // SimPEL.g:37:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__70

    // $ANTLR start T__71
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:38:7: ( '>' )
            // SimPEL.g:38:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__71

    // $ANTLR start T__72
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:39:7: ( '<=' )
            // SimPEL.g:39:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__72

    // $ANTLR start T__73
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:40:7: ( '>=' )
            // SimPEL.g:40:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__73

    // $ANTLR start T__74
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:41:7: ( '+' )
            // SimPEL.g:41:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__74

    // $ANTLR start T__75
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:42:7: ( '-' )
            // SimPEL.g:42:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__75

    // $ANTLR start T__76
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:43:7: ( '*' )
            // SimPEL.g:43:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__76

    // $ANTLR start T__77
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:44:7: ( '/' )
            // SimPEL.g:44:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__77

    // $ANTLR start T__78
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:45:7: ( '.' )
            // SimPEL.g:45:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end T__78

    // $ANTLR start EXT_EXPR
    public final void mEXT_EXPR() throws RecognitionException {
        try {
            int _type = EXT_EXPR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:131:2: ( '[' ( options {greedy=false; } : . )* ']' )
            // SimPEL.g:131:4: '[' ( options {greedy=false; } : . )* ']'
            {
            match('['); 
            // SimPEL.g:131:8: ( options {greedy=false; } : . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==']') ) {
                    alt1=2;
                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='\\')||(LA1_0>='^' && LA1_0<='\uFFFE')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // SimPEL.g:131:35: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end EXT_EXPR

    // $ANTLR start VAR_MODS
    public final void mVAR_MODS() throws RecognitionException {
        try {
            int _type = VAR_MODS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:134:9: ( 'string' | 'int' | 'float' )
            int alt2=3;
            switch ( input.LA(1) ) {
            case 's':
                {
                alt2=1;
                }
                break;
            case 'i':
                {
                alt2=2;
                }
                break;
            case 'f':
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // SimPEL.g:134:11: 'string'
                    {
                    match("string"); 


                    }
                    break;
                case 2 :
                    // SimPEL.g:134:22: 'int'
                    {
                    match("int"); 


                    }
                    break;
                case 3 :
                    // SimPEL.g:134:30: 'float'
                    {
                    match("float"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end VAR_MODS

    // $ANTLR start SEMI
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:135:6: ( ';' )
            // SimPEL.g:135:8: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end SEMI

    // $ANTLR start ID
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:136:4: ( ( LETTER | '_' ) ( LETTER | DIGIT | '_' | '-' )* )
            // SimPEL.g:136:6: ( LETTER | '_' ) ( LETTER | DIGIT | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // SimPEL.g:136:22: ( LETTER | DIGIT | '_' | '-' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='-'||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // SimPEL.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end ID

    // $ANTLR start INT
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:137:5: ( ( DIGIT )+ )
            // SimPEL.g:137:7: ( DIGIT )+
            {
            // SimPEL.g:137:7: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // SimPEL.g:137:8: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end INT

    // $ANTLR start STRING
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:138:8: ( '\"' ( ESCAPE_SEQ | ~ ( '\\\\' | '\"' ) )* '\"' )
            // SimPEL.g:138:10: '\"' ( ESCAPE_SEQ | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // SimPEL.g:138:14: ( ESCAPE_SEQ | ~ ( '\\\\' | '\"' ) )*
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\\') ) {
                    alt5=1;
                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFE')) ) {
                    alt5=2;
                }


                switch (alt5) {
            	case 1 :
            	    // SimPEL.g:138:16: ESCAPE_SEQ
            	    {
            	    mESCAPE_SEQ(); 

            	    }
            	    break;
            	case 2 :
            	    // SimPEL.g:138:29: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end STRING

    // $ANTLR start ESCAPE_SEQ
    public final void mESCAPE_SEQ() throws RecognitionException {
        try {
            int _type = ESCAPE_SEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:140:2: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) )
            // SimPEL.g:140:4: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
            {
            match('\\'); 
            if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end ESCAPE_SEQ

    // $ANTLR start SL_COMMENTS
    public final void mSL_COMMENTS() throws RecognitionException {
        try {
            int _type = SL_COMMENTS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:143:2: ( ( '#' | '//' ) ( . )* CR )
            // SimPEL.g:143:4: ( '#' | '//' ) ( . )* CR
            {
            // SimPEL.g:143:4: ( '#' | '//' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='#') ) {
                alt6=1;
            }
            else if ( (LA6_0=='/') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // SimPEL.g:143:5: '#'
                    {
                    match('#'); 

                    }
                    break;
                case 2 :
                    // SimPEL.g:143:9: '//'
                    {
                    match("//"); 


                    }
                    break;

            }

            // SimPEL.g:143:15: ( . )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\n'||LA7_0=='\r') ) {
                    alt7=2;
                }
                else if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\f')||(LA7_0>='\u000E' && LA7_0<='\uFFFE')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // SimPEL.g:143:15: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            mCR(); 
             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end SL_COMMENTS

    // $ANTLR start CR
    public final void mCR() throws RecognitionException {
        try {
            int _type = CR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:144:4: ( ( '\\r' | '\\n' )+ )
            // SimPEL.g:144:6: ( '\\r' | '\\n' )+
            {
            // SimPEL.g:144:6: ( '\\r' | '\\n' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='\n'||LA8_0=='\r') ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // SimPEL.g:
            	    {
            	    if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end CR

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SimPEL.g:145:4: ( ( ' ' | '\\t' )+ )
            // SimPEL.g:145:6: ( ' ' | '\\t' )+
            {
            // SimPEL.g:145:6: ( ' ' | '\\t' )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='\t'||LA9_0==' ') ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // SimPEL.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);

             skip(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start DIGIT
    public final void mDIGIT() throws RecognitionException {
        try {
            // SimPEL.g:147:5: ( '0' .. '9' )
            // SimPEL.g:147:10: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end DIGIT

    // $ANTLR start LETTER
    public final void mLETTER() throws RecognitionException {
        try {
            // SimPEL.g:149:5: ( 'a' .. 'z' | 'A' .. 'Z' )
            // SimPEL.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end LETTER

    public void mTokens() throws RecognitionException {
        // SimPEL.g:1:8: ( T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | EXT_EXPR | VAR_MODS | SEMI | ID | INT | STRING | ESCAPE_SEQ | SL_COMMENTS | CR | WS )
        int alt10=45;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // SimPEL.g:1:10: T__44
                {
                mT__44(); 

                }
                break;
            case 2 :
                // SimPEL.g:1:16: T__45
                {
                mT__45(); 

                }
                break;
            case 3 :
                // SimPEL.g:1:22: T__46
                {
                mT__46(); 

                }
                break;
            case 4 :
                // SimPEL.g:1:28: T__47
                {
                mT__47(); 

                }
                break;
            case 5 :
                // SimPEL.g:1:34: T__48
                {
                mT__48(); 

                }
                break;
            case 6 :
                // SimPEL.g:1:40: T__49
                {
                mT__49(); 

                }
                break;
            case 7 :
                // SimPEL.g:1:46: T__50
                {
                mT__50(); 

                }
                break;
            case 8 :
                // SimPEL.g:1:52: T__51
                {
                mT__51(); 

                }
                break;
            case 9 :
                // SimPEL.g:1:58: T__52
                {
                mT__52(); 

                }
                break;
            case 10 :
                // SimPEL.g:1:64: T__53
                {
                mT__53(); 

                }
                break;
            case 11 :
                // SimPEL.g:1:70: T__54
                {
                mT__54(); 

                }
                break;
            case 12 :
                // SimPEL.g:1:76: T__55
                {
                mT__55(); 

                }
                break;
            case 13 :
                // SimPEL.g:1:82: T__56
                {
                mT__56(); 

                }
                break;
            case 14 :
                // SimPEL.g:1:88: T__57
                {
                mT__57(); 

                }
                break;
            case 15 :
                // SimPEL.g:1:94: T__58
                {
                mT__58(); 

                }
                break;
            case 16 :
                // SimPEL.g:1:100: T__59
                {
                mT__59(); 

                }
                break;
            case 17 :
                // SimPEL.g:1:106: T__60
                {
                mT__60(); 

                }
                break;
            case 18 :
                // SimPEL.g:1:112: T__61
                {
                mT__61(); 

                }
                break;
            case 19 :
                // SimPEL.g:1:118: T__62
                {
                mT__62(); 

                }
                break;
            case 20 :
                // SimPEL.g:1:124: T__63
                {
                mT__63(); 

                }
                break;
            case 21 :
                // SimPEL.g:1:130: T__64
                {
                mT__64(); 

                }
                break;
            case 22 :
                // SimPEL.g:1:136: T__65
                {
                mT__65(); 

                }
                break;
            case 23 :
                // SimPEL.g:1:142: T__66
                {
                mT__66(); 

                }
                break;
            case 24 :
                // SimPEL.g:1:148: T__67
                {
                mT__67(); 

                }
                break;
            case 25 :
                // SimPEL.g:1:154: T__68
                {
                mT__68(); 

                }
                break;
            case 26 :
                // SimPEL.g:1:160: T__69
                {
                mT__69(); 

                }
                break;
            case 27 :
                // SimPEL.g:1:166: T__70
                {
                mT__70(); 

                }
                break;
            case 28 :
                // SimPEL.g:1:172: T__71
                {
                mT__71(); 

                }
                break;
            case 29 :
                // SimPEL.g:1:178: T__72
                {
                mT__72(); 

                }
                break;
            case 30 :
                // SimPEL.g:1:184: T__73
                {
                mT__73(); 

                }
                break;
            case 31 :
                // SimPEL.g:1:190: T__74
                {
                mT__74(); 

                }
                break;
            case 32 :
                // SimPEL.g:1:196: T__75
                {
                mT__75(); 

                }
                break;
            case 33 :
                // SimPEL.g:1:202: T__76
                {
                mT__76(); 

                }
                break;
            case 34 :
                // SimPEL.g:1:208: T__77
                {
                mT__77(); 

                }
                break;
            case 35 :
                // SimPEL.g:1:214: T__78
                {
                mT__78(); 

                }
                break;
            case 36 :
                // SimPEL.g:1:220: EXT_EXPR
                {
                mEXT_EXPR(); 

                }
                break;
            case 37 :
                // SimPEL.g:1:229: VAR_MODS
                {
                mVAR_MODS(); 

                }
                break;
            case 38 :
                // SimPEL.g:1:238: SEMI
                {
                mSEMI(); 

                }
                break;
            case 39 :
                // SimPEL.g:1:243: ID
                {
                mID(); 

                }
                break;
            case 40 :
                // SimPEL.g:1:246: INT
                {
                mINT(); 

                }
                break;
            case 41 :
                // SimPEL.g:1:250: STRING
                {
                mSTRING(); 

                }
                break;
            case 42 :
                // SimPEL.g:1:257: ESCAPE_SEQ
                {
                mESCAPE_SEQ(); 

                }
                break;
            case 43 :
                // SimPEL.g:1:268: SL_COMMENTS
                {
                mSL_COMMENTS(); 

                }
                break;
            case 44 :
                // SimPEL.g:1:280: CR
                {
                mCR(); 

                }
                break;
            case 45 :
                // SimPEL.g:1:283: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\1\uffff\1\41\2\uffff\1\52\3\uffff\2\41\1\uffff\1\55\10\41\3\uffff"+
        "\1\71\1\73\3\uffff\1\74\2\uffff\1\41\7\uffff\2\41\2\uffff\2\41\2"+
        "\uffff\11\41\5\uffff\5\41\1\123\4\41\1\130\5\41\1\136\5\41\1\uffff"+
        "\4\41\1\uffff\3\41\1\153\1\41\1\uffff\6\41\1\163\2\41\1\123\1\166"+
        "\1\167\1\uffff\4\41\1\174\1\175\1\41\1\uffff\2\41\2\uffff\1\41\1"+
        "\123\1\u0082\1\u0083\2\uffff\1\u0084\1\41\1\u0086\1\41\3\uffff\1"+
        "\41\1\uffff\1\41\1\u008a\1\u008b\2\uffff";
    static final String DFA10_eofS =
        "\u008c\uffff";
    static final String DFA10_minS =
        "\1\11\1\141\2\uffff\1\75\3\uffff\1\154\1\156\1\uffff\1\0\1\145\1"+
        "\156\1\154\1\156\1\150\1\170\2\141\3\uffff\2\75\3\uffff\1\57\2\uffff"+
        "\1\164\7\uffff\1\157\1\162\2\uffff\1\151\1\164\2\uffff\1\143\1\101"+
        "\1\162\1\157\1\164\1\162\1\151\1\155\1\162\5\uffff\1\162\1\143\1"+
        "\164\1\145\1\157\1\55\1\145\1\154\1\145\1\154\1\55\1\141\1\151\1"+
        "\157\1\164\1\145\1\55\1\151\1\145\2\156\1\153\1\uffff\1\151\1\171"+
        "\1\163\1\141\1\uffff\1\164\1\154\1\167\1\55\1\163\1\uffff\1\156"+
        "\1\163\1\145\1\164\1\145\1\166\1\55\1\163\1\162\3\55\1\uffff\1\160"+
        "\1\147\1\163\1\162\2\55\1\145\1\uffff\1\141\1\155\2\uffff\1\141"+
        "\3\55\2\uffff\1\55\1\147\1\55\1\143\3\uffff\1\145\1\uffff\1\145"+
        "\2\55\2\uffff";
    static final String DFA10_maxS =
        "\1\175\1\162\2\uffff\1\75\3\uffff\1\154\1\156\1\uffff\1\ufffe\1"+
        "\145\1\156\1\157\1\156\1\150\1\170\2\141\3\uffff\2\75\3\uffff\1"+
        "\57\2\uffff\1\164\7\uffff\1\157\1\162\2\uffff\1\151\1\166\2\uffff"+
        "\1\160\1\115\1\162\1\157\1\164\1\162\1\151\1\155\1\162\5\uffff\1"+
        "\162\1\143\1\164\1\145\1\157\1\172\1\145\1\154\1\145\1\154\1\172"+
        "\1\141\1\151\1\157\1\164\1\145\1\172\1\151\1\145\2\156\1\153\1\uffff"+
        "\1\151\1\171\1\163\1\141\1\uffff\1\164\1\154\1\167\1\172\1\163\1"+
        "\uffff\1\156\1\163\1\145\1\164\1\145\1\166\1\172\1\163\1\162\3\172"+
        "\1\uffff\1\160\1\147\1\163\1\162\2\172\1\145\1\uffff\1\141\1\155"+
        "\2\uffff\1\141\3\172\2\uffff\1\172\1\147\1\172\1\143\3\uffff\1\145"+
        "\1\uffff\1\145\2\172\2\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\1\3\1\uffff\1\6\1\7\1\10\2\uffff\1\13\11\uffff\1\27"+
        "\1\30\1\32\2\uffff\1\37\1\40\1\41\1\uffff\1\43\1\44\1\uffff\1\46"+
        "\1\47\1\50\1\51\1\52\1\54\1\55\2\uffff\1\31\1\5\2\uffff\1\14\1\53"+
        "\11\uffff\1\35\1\33\1\36\1\34\1\42\26\uffff\1\45\4\uffff\1\21\5"+
        "\uffff\1\26\14\uffff\1\24\7\uffff\1\16\2\uffff\1\22\1\23\4\uffff"+
        "\1\11\1\12\4\uffff\1\1\1\4\1\15\1\uffff\1\20\3\uffff\1\17\1\25";
    static final String DFA10_specialS =
        "\u008c\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\46\1\45\2\uffff\1\45\22\uffff\1\46\1\26\1\43\1\13\4\uffff"+
            "\1\5\1\7\1\33\1\31\1\12\1\32\1\35\1\34\12\42\1\25\1\40\1\27"+
            "\1\4\1\30\1\uffff\1\6\32\41\1\36\1\44\2\uffff\1\41\1\uffff\2"+
            "\41\1\10\1\41\1\21\1\16\2\41\1\11\4\41\1\22\1\15\1\1\1\41\1"+
            "\14\1\37\1\20\1\17\1\23\4\41\1\2\1\24\1\3",
            "\1\50\20\uffff\1\47",
            "",
            "",
            "\1\51",
            "",
            "",
            "",
            "\1\53",
            "\1\54",
            "",
            "\uffff\56",
            "\1\57",
            "\1\60",
            "\1\62\2\uffff\1\61",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "",
            "",
            "",
            "\1\70",
            "\1\72",
            "",
            "",
            "",
            "\1\56",
            "",
            "",
            "\1\75",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\76",
            "\1\77",
            "",
            "",
            "\1\100",
            "\1\102\1\uffff\1\101",
            "",
            "",
            "\1\103\14\uffff\1\104",
            "\1\106\13\uffff\1\105",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "",
            "",
            "",
            "",
            "",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\154",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\164",
            "\1\165",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\176",
            "",
            "\1\177",
            "\1\u0080",
            "",
            "",
            "\1\u0081",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "",
            "",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\u0085",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\u0087",
            "",
            "",
            "",
            "\1\u0088",
            "",
            "\1\u0089",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "\1\41\2\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | EXT_EXPR | VAR_MODS | SEMI | ID | INT | STRING | ESCAPE_SEQ | SL_COMMENTS | CR | WS );";
        }
    }
 

}