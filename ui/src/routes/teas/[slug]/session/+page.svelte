<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import SessionBottomBar from './SessionBottomBar.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import TimerBar from './TimerBar.svelte';
	import type { Session } from './types';

	let activeInfusionCounter = $state(0);
	let sessions = $state<Session>({
		infusions: [
			{
				startTime: new Date(),
				infusionTime: 24,
				rating: 3,
				tastingCategories: [
					{
						name: 'Eye',
						subCategories: [{ name: 'Wet Leaf', notes: ['Green', 'Curly'] }]
					}
				]
			}
		]
	});

	let activeInfusion = $derived(sessions.infusions.at(activeInfusionCounter));
	let isTimerRunning = $state(false);
</script>

<div class="flex w-full flex-1 flex-col items-center justify-between gap-6">
	<div>header</div>
	<div class="flex w-full flex-1 flex-col gap-2">
		{#if (activeInfusion?.tastingCategories.length ?? 0) > 0}
			{#each activeInfusion?.tastingCategories as category (category.name)}
				<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>
				{#each category.subCategories as subCategory (subCategory.name)}
					<TastingNoteDisplay {subCategory} />
				{/each}
			{/each}
		{:else}
			<Button label="Add your first tasting note" class="btn-dash" />
		{/if}
	</div>
	<TimerBar {activeInfusion} bind:isTimerRunning />
	<SessionBottomBar
		disabled={isTimerRunning}
		bind:infusions={sessions.infusions}
		bind:activeInfusion={activeInfusionCounter}
	/>
</div>
