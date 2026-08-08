<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { Pause, Play, X } from '@lucide/svelte';
	import type { Infusion } from './types';
	import Checkbox from '$lib/basics/Checkbox.svelte';

	let {
		activeInfusion,
		// eslint-disable-next-line no-useless-assignment
		isTimerRunning = $bindable(false)
	}: { activeInfusion?: Infusion; isTimerRunning: boolean } = $props();

	let mode = $state(0);
	let timer = $state(0);
	let totalTime = $state(0);
	let direction = 1;

	let targetTime = $state(20);
	let blindMode = $state(false);
	let timingDone = $derived((activeInfusion?.infusionTime ?? 0) > 0);

	let interval: ReturnType<typeof setInterval> | null = $state(null);

	function startTimer() {
		if (interval) {
			stopTimer();
			return;
		}

		timingDone = false;
		isTimerRunning = true;
		if (mode == 0) {
			timer = targetTime;
			direction = -1;
		} else {
			timer = 0;
			direction = 1;
		}

		totalTime = 0;
		interval = setInterval(() => {
			timer += 0.1 * direction;
			totalTime += 0.1;

			if (timer <= 0) {
				totalTime += timer;
				timer = 0;
				stopTimer();
			}
		}, 100);
	}

	function stopTimer() {
		if (activeInfusion) {
			activeInfusion!.infusionTime = parseFloat(totalTime.toFixed(2));
		}
		clearInterval(interval!);
		interval = null;
		isTimerRunning = false;
		timingDone = true;
	}

	function resetTimer() {
		timingDone = false;
		if (activeInfusion) {
			activeInfusion!.infusionTime = undefined;
		}
	}
</script>

<div class="flex w-full flex-col gap-4 md:flex-row md:gap-6">
	{#if timingDone}
		<div class="join flex">
			<div class="flex-1">
				<Input
					inputClass="w-full"
					placeholder="Brewing Time (s)"
					step={0.01}
					type="number"
					bind:value={activeInfusion!.infusionTime}
				/>
			</div>
			<Button class="join-item" icon={X} onclick={resetTimer} />
		</div>
	{:else}
		<div class="flex w-full items-center gap-6 md:w-fit">
			<div class="join">
				<input
					name="options"
					class="btn join-item"
					aria-label="Timer"
					checked={mode == 0}
					disabled={interval !== null}
					onclick={() => (mode = 0)}
					type="radio"
				/>
				<input
					name="options"
					class="btn join-item"
					aria-label="Stopwatch"
					checked={mode == 1}
					disabled={interval !== null}
					onclick={() => (mode = 1)}
					type="radio"
				/>
			</div>

			{#if mode == 0}
				<Input
					disabled={interval !== null}
					placeholder="Target Time (s)"
					type="number"
					bind:value={targetTime}
				/>
			{:else}
				<Checkbox disabled={interval !== null} label="Blind Mode" bind:value={blindMode} />
			{/if}
		</div>
		<Button class="w-full btn-primary md:w-18" icon={interval ? Pause : Play} onclick={startTimer}>
			{#if interval && !blindMode}
				{timer.toFixed(1) + 's'}
			{/if}
		</Button>
	{/if}
</div>
