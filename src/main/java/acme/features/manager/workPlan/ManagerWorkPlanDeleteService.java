package acme.features.manager.workPlan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.consistsOf.ConsistsOf;
import acme.entities.roles.Manager;
import acme.entities.workPlans.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractDeleteService;

@Service
public class ManagerWorkPlanDeleteService implements AbstractDeleteService<Manager, WorkPlan> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerWorkPlanRepository repository;



	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		assert request != null;

		boolean result;
		int workPlanId;
		WorkPlan workPlan;
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

		request.unbind(entity, model, "initialTime", "finalTime", "workload","publicWorkPlan");
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
	public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void delete(final Request<WorkPlan> request, final WorkPlan entity) {
		assert request != null;
		assert entity != null;
		List<ConsistsOf> consistsOfAEliminar;
		consistsOfAEliminar = this.repository.findManyConsistsOfById(entity.getId());
		for(int i=0; i<consistsOfAEliminar.size();i++) {
			this.repository.delete(consistsOfAEliminar.get(i));
		}
		this.repository.delete(entity);
	}

}
