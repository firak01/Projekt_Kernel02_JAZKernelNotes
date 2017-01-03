package basic.zNotes.basic;

import java.util.Vector;

import lotus.domino.Agent;
import lotus.domino.NotesException;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zKernel.KernelZZZ;

public class AgentZZZ extends KernelUseObjectZZZ{
		private Agent objAgentReal = null;
		private String sAgentName = null;
		private String sAgentAlias = null;
		
		public AgentZZZ(KernelZZZ objKernel, Agent objAgent) throws ExceptionZZZ{
			super(objKernel);
			if(objAgent==null){
				ExceptionZZZ ez = new ExceptionZZZ("Real Agent handle", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.objAgentReal = objAgent;
		}
		
		public AgentZZZ(KernelZZZ objKernel, String sAgentName) throws ExceptionZZZ{
			super(objKernel);
			if(StringZZZ.isEmpty(sAgentName)){
				ExceptionZZZ ez = new ExceptionZZZ("Faked agent name", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.sAgentName = sAgentName;
		}
		
		public String getAgentName() throws NotesException{
			String sReturn = null;
			if(StringZZZ.isEmpty(this.sAgentName)){
				if(this.objAgentReal!=null){
					String stemp = this.objAgentReal.getName();
					
					//Den Agentennamen nun auflösen
					Vector objVectorAliasAll = AgentZZZ.solveAgentAliasAll(stemp);
					this.sAgentName = (String) objVectorAliasAll.get(1);
				}
			}
			sReturn = this.sAgentName;
			return sReturn;
		}
		
		public String getAgentAliasFirst() throws NotesException{
			String sReturn = null;
			if(StringZZZ.isEmpty(this.sAgentAlias)){
				if(this.objAgentReal!=null){
					//Fall: Echter Notes-Agent
					String stemp = this.objAgentReal.getName();
					
					//Den Agentennamen nun auflösen
					Vector objVectorAliasAll = AgentZZZ.solveAgentAliasAll(stemp);
					if(objVectorAliasAll.size()>=2){
						this.sAgentAlias = (String) objVectorAliasAll.get(1);
					}else{
						this.sAgentAlias = (String) objVectorAliasAll.get(0);
					}
				}else{
					//Fall: Faken des Notes-Agenten. Z.B. beim Servlet
					this.sAgentAlias = this.sAgentName;
				}
			}
			sReturn = this.sAgentAlias;
			return sReturn;
		}
		
		public Agent getAgent() {
			return this.objAgentReal;
		}
		
		public static Vector solveAgentAliasAll(String sAgentNameTotal){
			Vector objReturn = new Vector();
			
			//TODO: Eigentlich einen StringTokenizer verwenden
//			Auflösen des Agentennamens, etc.
			int itemp = sAgentNameTotal.indexOf("|");
			if(itemp >= 0){
				objReturn.add(sAgentNameTotal.substring(0,itemp));
				objReturn.add( sAgentNameTotal.substring(itemp + 1));				
			}else{
				objReturn.add(sAgentNameTotal);			
			}
			return objReturn;
		}
			
}//END class
