import type { InfusionDto } from '$lib/api/gen/types';

class TimerState {
	target = $state<InfusionDto>();
	startedAt = $state<number>();
	now = $state(Date.now());

	mode = $state<0 | 1>(readPreference().mode);
	targetTime = $state(20);
	blind = $state(readPreference().blind);

	running = $derived(this.startedAt !== undefined);
	elapsed = $derived(this.running ? (this.now - this.startedAt!) / 1000 : 0);
	display = $derived(this.mode === 0 ? Math.max(0, this.targetTime - this.elapsed) : this.elapsed);

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
			if (this.mode == 0 && this.elapsed >= this.targetTime) this.stop();
		}, 100);
	}

	stop() {
		clearInterval(this.#interval);
		this.now = Date.now();
		if (!this.target) return;

		this.target!.infusionTime = parseFloat(this.elapsed.toFixed(2));
		if (this.mode === 0 && this.target!.infusionTime > this.targetTime) {
			this.target!.infusionTime = this.targetTime;
			console.log(this.targetTime);
		}

		this.startedAt = undefined;
		this.target = undefined;
	}

	setMode(newMode: 0 | 1) {
		timerState.mode = newMode;
		localStorage.setItem('timerbar_mode', newMode + '');
	}

	toggleBlind() {
		timerState.blind = !timerState.blind;
		localStorage.setItem('timerbar_blind', timerState.blind + '');
	}
}

export const timerState = new TimerState();

function readPreference() {
	const storedMode = localStorage.getItem('timerbar_mode') ?? '0';
	const parsedMode = parseInt(storedMode);
	const mode: 0 | 1 = parsedMode === 1 ? 1 : 0;

	const storedBlindMode = localStorage.getItem('timerbar_blind') ?? 'false';
	const blind = storedBlindMode === 'true';

	return { mode, blind };
}
