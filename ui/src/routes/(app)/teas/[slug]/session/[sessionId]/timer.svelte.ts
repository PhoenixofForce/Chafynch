import type { InfusionDto } from '$lib/api/gen/types';

class TimerState {
	target = $state<InfusionDto>();
	startedAt = $state<number>();
	now = $state(Date.now());

	mode = $state<'timer' | 'stopwatch'>(readPreference().mode);
	targetTime = $state(20);
	blind = $state(readPreference().blind);

	running = $derived(this.startedAt !== undefined);
	elapsed = $derived(this.running ? (this.now - this.startedAt!) / 1000 : 0);
	display = $derived(
		this.mode === 'timer' ? Math.max(0, this.targetTime - this.elapsed) : this.elapsed
	);

	#interval?: ReturnType<typeof setInterval>;

	toggle(infusion: InfusionDto) {
		if (this.running) {
			this.stop();
			return;
		}

		this.target = infusion;
		this.startedAt = Date.now();
		this.now = Date.now();

		this.#interval = setInterval(() => {
			this.now = Date.now();
			if (this.mode === 'timer' && this.elapsed >= this.targetTime) this.stop();
		}, 100);
	}

	stop() {
		clearInterval(this.#interval);
		this.now = Date.now();
		if (!this.target) return;

		this.target!.infusionTime = parseFloat(this.elapsed.toFixed(2));
		if (this.mode === 'timer' && this.target!.infusionTime > this.targetTime) {
			this.target!.infusionTime = this.targetTime;
		}

		this.startedAt = undefined;
		this.target = undefined;
	}

	setMode(newMode: 'timer' | 'stopwatch') {
		this.mode = newMode;
		localStorage.setItem('timerbar_mode', newMode + '');
	}

	toggleBlind() {
		this.blind = !this.blind;
		localStorage.setItem('timerbar_blind', this.blind + '');
	}
}

export const timerState = new TimerState();

function readPreference() {
	const storedMode = localStorage.getItem('timerbar_mode') ?? 'timer';
	const mode: 'timer' | 'stopwatch' =
		storedMode === '1' || storedMode === 'stopwatch' ? 'stopwatch' : 'timer'; // todo: remove backwards compatibility later

	const storedBlindMode = localStorage.getItem('timerbar_blind') ?? 'false';
	const blind = storedBlindMode === 'true';

	return { mode, blind };
}
