package ab.tester;

import android.content.SharedPreferences;

public class ABTest {

	private static final String SEPERATOR = "___";
	protected String testName;
	protected ABVariable[] variables;
	protected boolean lock;

	public ABTest(String testName, boolean lockVariables ,String... desiredVariables) {
		this.lock = lockVariables;
		this.testName = testName;
		this.variables = new ABVariable[desiredVariables.length];
		for (int i = 0; i < desiredVariables.length; i++)
			variables[i] = new ABVariable(desiredVariables[i]);
	}
	
	/**
	 * Saves the ABTest data into the SharedPreferences
	 * for further with getVariable();  
	 * @param sp
	 */
	protected void saveTo(SharedPreferences sp) {
		SharedPreferences.Editor spe = sp.edit();
		for (int i = 0; i < variables.length; i++) {
			String key = testName + SEPERATOR + variables[i].name;
			
			// save only if not locked, or locked and yet to be set
			if( ( lock == false ) ||
				( lock == true && sp.contains(key)) ){
				spe.putString(key , variables[i].value );
			}
		}
		// set it to true, so we can check that that has been downloaded
		spe.putBoolean(testName, true);
		spe.commit();
	}
	
	/**
	 * used to store the data
	 */
	public class ABVariable {
		protected String name,value;
		protected ABVariable(String name){
			this.name = name;
		}
	}

	/**
	 * Checks if the AB test is synced
	 * @param sp
	 * @param ABTestName - the name of experimetn 
	 * @return return if the AB test is synced
	 */
	protected static boolean isABTestSynced(SharedPreferences sp,String ABTestName){
		return sp.getBoolean(ABTestName, false);
	}
	
	/**
	 * get the variable synced previously to the SharedPreferences
	 * fetched localy
	 * @param sp - the SharedPreferences
	 * @param abName - the name of the ABtest( project name)
	 * @param variableName - the name of the varaible to be fetched
	 * @param defaultValue - the default will be returned when, the value is not synced. you can check if synced via isABTestSynced();
	 * @return
	 */
	protected static String getVariable(SharedPreferences sp,String abName,String variableName,String defaultValue){
		return sp.getString(abName + SEPERATOR + variableName, defaultValue);
	}
	
}