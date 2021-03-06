package acme.features.manager.workPlan;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Manager;
import acme.entities.workPlans.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class ManagerWorkPlanUpdateService implements AbstractUpdateService<Manager, WorkPlan> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerWorkPlanRepository repository;



	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		assert request != null;

		boolean result;
		int workPlanId;
		final WorkPlan workPlan;
		Manager manager;
		Principal principal;

		workPlanId = request.getModel().getInteger("id");
		workPlan = this.repository.findOneWorkPlanById(workPlanId);
		manager = workPlan.getManager();
		principal = request.getPrincipal();
		result = manager.getUserAccount().getId() == principal.getAccountId() && !workPlan.isPublished();

		return result;
	}

	@Override
	public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		WorkPlan workPlan;
		workPlan = this.repository.findOneWorkPlanById(entity.getId());
		if (!errors.hasErrors("initialTime")) {
			Calendar calendar;
			Date actualDate;
						
			calendar = new GregorianCalendar();
			actualDate = calendar.getTime();
			
			errors.state(request, workPlan.getInitialTime().compareTo(entity.getInitialTime())==0 || entity.getInitialTime().after(actualDate), "initialTime", "manager.work-plan.form.error.initialTime");
		}
		
		if (!errors.hasErrors("finalTime")) {
			Calendar calendar;
			Date actualDate;
						
			calendar = new GregorianCalendar();
			actualDate = calendar.getTime();
			errors.state(request, workPlan.getFinalTime().compareTo(entity.getFinalTime())==0 || entity.getFinalTime().after(actualDate), "finalTime", "manager.work-plan.form.error.finalTime");
			if(entity.getInitialTime() != null) {
				errors.state(request, entity.getFinalTime().after(entity.getInitialTime()), "finalTime", "manager.work-plan.form.error.finalTimeInitial");
			}
		}
		
		if (!errors.hasErrors("publicWorkPlan")) {
			
			List<Boolean> tasks;
			tasks = this.repository.findManyTasksPrivateById(entity.getId());
			
			errors.state(request, !entity.isPublicWorkPlan() || (entity.isPublicWorkPlan() && tasks.isEmpty()), "publicWorkPlan", "manager.work-plan.form.error.publicWorkPlan");
		}
	
	}

	@Override
	public void bind(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "initialTime", "finalTime","publicWorkPlan","published");

	}

	@Override
	public WorkPlan findOne(final Request<WorkPlan> request) {
		assert request != null;

		WorkPlan result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneWorkPlanById(id);

		return result;
	}

	@Override
	public void update(final Request<WorkPlan> request, final WorkPlan entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}
