package acme.testing.generic.spam;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;

public class GenericSpamManagerTaskUpdateTest extends AcmePlannerTest{

	/*
	Este test actualiza un task. Comprueba que las task que se quiere actualizar
	existe en el listado, la modifica y actualiza, y posteriormente comprueba que
	la actualización ha sido realizada con éxito.
	Se espera un resultado positivo.
	 */
	@ParameterizedTest
	@CsvFileSource(resources = "/generic/spam/ManagerTaskUpdatePositive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)	
	public void updatePositive(final int recordIndex, final String prevTitle, final String prevInitialTime, final String prevFinalTime, final String prevWorkload, final String prevLink, final String prevDescription, final String prevPublicTask, final String prevPublicTaskShow,
		final String newTitle, final String newInitialTime, final String newFinalTime, final String newWorkload, final String newLink, final String newDescription, final String newPublicTask, final String newPublicTaskShow, final String newValue) {		
		
		super.signIn("manager", "asdf1234");
		super.clickOnMenu("Manager", "See tasks");

		super.checkColumnHasValue(recordIndex, 0, prevTitle);
		super.checkColumnHasValue(recordIndex, 1, prevInitialTime);
		super.checkColumnHasValue(recordIndex, 2, prevFinalTime);
		super.checkColumnHasValue(recordIndex, 3, prevWorkload);
		super.checkColumnHasValue(recordIndex, 4, prevLink);
		super.checkColumnHasValue(recordIndex, 5, prevDescription);
		super.checkColumnHasValue(recordIndex, 6, prevPublicTask);
			
		super.clickOnListingRecord(recordIndex);
		
		super.fillInputBoxIn("title", newTitle);
		super.fillInputBoxIn("initialTime", newInitialTime);
		super.fillInputBoxIn("finalTime", newFinalTime);
		super.fillInputBoxIn("workload", newWorkload);
		super.fillInputBoxIn("description", newDescription);
		super.fillInputBoxIn("link", newLink);
		super.fillInputBoxIn("publicTask", newValue);
		
		super.clickOnSubmitButton("Update Task");
		
		super.checkColumnHasValue(recordIndex, 0, newTitle);
		super.checkColumnHasValue(recordIndex, 1, newInitialTime);
		super.checkColumnHasValue(recordIndex, 2, newFinalTime);
		super.checkColumnHasValue(recordIndex, 3, newWorkload);
		super.checkColumnHasValue(recordIndex, 4, newLink);
		super.checkColumnHasValue(recordIndex, 5, newDescription);
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("title", newTitle);
		super.checkInputBoxHasValue("initialTime", newInitialTime);
		super.checkInputBoxHasValue("finalTime", newFinalTime);
		super.checkInputBoxHasValue("workload", newWorkload);
		super.checkInputBoxHasValue("description", newDescription);
		super.checkInputBoxHasValue("link", newLink);
		
		
		super.signOut();
	}
	
	/*
		Este test intenta actualizar una task sin cumplir las validaciones de spam.
		Se espera un resultado negativo.
	*/
	@ParameterizedTest
	@CsvFileSource(resources = "/generic/spam/ManagerTaskUpdateNegative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void updateNegative(final int recordIndex, final String prevTitle, final String prevInitialTime, final String prevFinalTime, final String prevWorkload, final String prevLink, final String prevDescription, final String prevPublicTask, final String prevPublicTaskShow,
		final String newTitle, final String newInitialTime, final String newFinalTime, final String newWorkload, final String newLink, final String newDescription, final String newPublicTask, final String newPublicTaskShow) {
		
		super.signIn("manager", "asdf1234");
		super.clickOnMenu("Manager", "See tasks");	
		
		super.checkColumnHasValue(recordIndex, 0, prevTitle);
		super.checkColumnHasValue(recordIndex, 1, prevInitialTime);
		super.checkColumnHasValue(recordIndex, 2, prevFinalTime);
		super.checkColumnHasValue(recordIndex, 3, prevWorkload);
		super.checkColumnHasValue(recordIndex, 4, prevLink);
		super.checkColumnHasValue(recordIndex, 5, prevDescription);
		super.checkColumnHasValue(recordIndex, 6, prevPublicTask);
			
		super.clickOnListingRecord(recordIndex);
		
		super.fillInputBoxIn("title", newTitle);
		super.fillInputBoxIn("initialTime", newInitialTime);
		super.fillInputBoxIn("finalTime", newFinalTime);
		super.fillInputBoxIn("workload", newWorkload);
		super.fillInputBoxIn("description", newDescription);
		super.fillInputBoxIn("link", newLink);
		
		super.clickOnSubmitButton("Update Task");

		super.checkErrorsExist();

		super.signOut();
	}
	
}
