<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { Pause, Play, X } from '@lucide/svelte';

	let mode = $state(0);
	let timer = $state(0);
	let totalTime = $state(0);
	let direction = 1;

	let targetTime = $state(20);
	let blindMode = $state(false);

	let interval: ReturnType<typeof setInterval> | null = $state(null);
	let timingDone = $derived(interval == null && totalTime > 0);

	function startTimer() {
		if (interval) {
			stopTimer();
			return;
		}

		if (mode == 0) {
			timer = targetTime;
			direction = -1;
		} else {
			timer = 0;
			direction = 1;
		}

		interval = setInterval(() => {
			timer += 0.1 * direction;
			totalTime += 0.1;
			if (timer <= 0) {
				timer = 0;
				stopTimer();
			}
		}, 100);
	}

	function stopTimer() {
		clearInterval(interval!);
		interval = null;
	}
</script>

<div class="flex w-full flex-col gap-4 md:flex-row md:gap-6">
	{#if timingDone}
		<div class="join flex">
			<div class="flex-1">
				<Input
					inputClass="w-full"
					disabled
					placeholder="Brewing Time (s)"
					value={totalTime.toFixed(2)}
				/>
			</div>
			<Button class="join-item" icon={X} onclick={() => (totalTime = 0)} />
		</div>
	{:else}
		<div class="flex w-full items-center gap-6 md:w-fit">
			<div class="join">
				<input
					class="btn join-item"
					type="radio"
					name="options"
					aria-label="Timer"
					checked={mode == 0}
					onclick={() => (mode = 0)}
					disabled={interval !== null}
				/>
				<input
					class="btn join-item"
					type="radio"
					name="options"
					aria-label="Stopwatch"
					checked={mode == 1}
					onclick={() => (mode = 1)}
					disabled={interval !== null}
				/>
			</div>

			{#if mode == 0}
				<Input
					type="number"
					placeholder="Target Time (s)"
					bind:value={targetTime}
					disabled={interval !== null}
				/>
			{:else}
				<label class="label">
					<input
						type="checkbox"
						class="checkbox"
						disabled={interval !== null}
						bind:checked={blindMode}
					/>
					Blind Mode
				</label>
			{/if}
		</div>
		<Button class="w-full btn-primary md:w-18" icon={interval ? Pause : Play} onclick={startTimer}>
			{#if interval && !blindMode}
				{timer.toFixed(1) + 's'}
			{/if}
		</Button>
	{/if}
</div>
