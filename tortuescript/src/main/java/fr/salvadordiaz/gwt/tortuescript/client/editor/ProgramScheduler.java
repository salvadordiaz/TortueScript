package fr.salvadordiaz.gwt.tortuescript.client.editor;

import java.util.Iterator;

import javax.inject.Inject;

import com.google.common.collect.Iterators;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class ProgramScheduler {

	private static final int ANIMATION_PERIOD = 20;

	private final Scheduler scheduler;

	private Iterator<ScheduledCommand> commandIterator = Iterators.emptyIterator();
	private boolean isCanceled = false;

	@Inject
	public ProgramScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	private final RepeatingCommand animationCommand = new RepeatingCommand() {
		@Override
		public boolean execute() {
			if (!isCanceled && commandIterator.hasNext()) {
				commandIterator.next().execute();
				return commandIterator.hasNext();
			}
			return false;
		}
	};

	public void execute(Iterable<ScheduledCommand> commands) {
		commandIterator = commands.iterator();
		scheduler.scheduleFixedPeriod(animationCommand, ANIMATION_PERIOD);
	}

	public void togglePlayPause() {
		isCanceled = !isCanceled;
		if (!isCanceled) {
			scheduler.scheduleFixedPeriod(animationCommand, 20);
		}
	}
}
