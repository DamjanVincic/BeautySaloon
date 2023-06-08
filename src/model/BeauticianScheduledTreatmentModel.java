package model;

import java.util.List;

import entity.ScheduledTreatment;
import entity.User;
import manage.ScheduledTreatmentManager;

public class BeauticianScheduledTreatmentModel extends BeauticianTreatmentModel {
	private static final long serialVersionUID = -7582428401268462773L;
	
	public BeauticianScheduledTreatmentModel(ScheduledTreatmentManager scheduledTreatmentManager, User user) {
		super(scheduledTreatmentManager, user);
	}
	
	public List<ScheduledTreatment> getTreatments() {
		return super.getScheduledTreatmentManager().getBeauticianSchedule(super.getBeautician().getId());
	}

}
