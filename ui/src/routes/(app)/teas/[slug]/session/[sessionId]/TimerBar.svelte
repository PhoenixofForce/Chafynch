<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { Pause, Play, X } from '@lucide/svelte';
	import Checkbox from '$lib/basics/Checkbox.svelte';
	import type { InfusionDto } from '$lib/api/gen/types';
	import { timerState } from './timer.svelte';

	let { activeInfusion }: { activeInfusion?: InfusionDto } = $props();
	let timingDone = $derived(!!(timerState.target ?? activeInfusion)?.infusionTime);

	function resetTimer() {
		activeInfusion!.infusionTime = undefined;
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
					checked={timerState.mode === 'timer'}
					disabled={timerState.running}
					onclick={() => timerState.setMode('timer')}
					type="radio"
				/>
				<input
					name="options"
					class="btn join-item"
					aria-label="Stopwatch"
					checked={timerState.mode === 'stopwatch'}
					disabled={timerState.running}
					onclick={() => timerState.setMode('stopwatch')}
					type="radio"
				/>
			</div>

			{#if timerState.mode === 'timer'}
				<Input
					disabled={timerState.running}
					placeholder="Target Time (s)"
					type="number"
					bind:value={timerState.targetTime}
				/>
			{:else}
				<Checkbox
					disabled={timerState.running}
					label="Blind Mode"
					onchange={() => timerState.toggleBlind()}
					value={timerState.blind}
				/>
			{/if}
		</div>
		<Button
			class="w-full md:w-18 {timerState.target === activeInfusion || !timerState.running
				? 'btn-primary'
				: 'btn-accent'}"
			icon={timerState.running ? Pause : Play}
			onclick={() => timerState.toggle(activeInfusion!)}
		>
			{#if timerState.running && (timerState.mode === 'timer' || !timerState.blind)}
				{timerState.display.toFixed(1) + 's'}
			{/if}
		</Button>
	{/if}
</div>
