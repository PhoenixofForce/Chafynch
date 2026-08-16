<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Checkbox from '$lib/basics/Checkbox.svelte';
	import Input from '$lib/basics/Input.svelte';
	import Rating from '$lib/basics/Rating.svelte';
	import { MapPin, User } from '@lucide/svelte';
	import SessionBottomBar from './SessionBottomBar.svelte';
	import StartSettings from './StartSettings.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import TastingNoteModal from './TastingNoteModal.svelte';
	import TimerBar from './TimerBar.svelte';
	import { categories, globalCategories, type Tabs } from './types.js';
	import EndSettings from './EndSettings.svelte';
	import { sessionService } from '$lib/api/session.service.js';

	let { data } = $props();
	let session = $state(data.session);
	let lastSaved = JSON.stringify($state.snapshot(session));

	let saveDebounce: ReturnType<typeof setTimeout> | undefined;
	$effect(() => {
		const snapshot = JSON.stringify($state.snapshot(session));
		if (snapshot === lastSaved) return;

		if (saveDebounce) clearTimeout(saveDebounce);
		saveDebounce = setTimeout(() => {
			sessionService.update(session);
			lastSaved = snapshot;
		}, 1000);

		return () => clearTimeout(saveDebounce);
	});

	let activeTab = $state<Tabs>({ tab: 'start' });

	let activeInfusion = $derived(
		activeTab.tab === 'infusion' ? session.infusions?.at(activeTab.index) : undefined
	);
	let isTimerRunning = $state(false);

	let tastingNoteModal = $state<ReturnType<typeof TastingNoteModal>>();
	let globalTastingNoteModal = $state<ReturnType<typeof TastingNoteModal>>();
	let hasNotes = $derived(Object.values(activeInfusion?.tastingNotes ?? {}).length > 0);
</script>

{#snippet infusionTab()}
	{#if hasNotes}
		{#each categories as category (category.name)}
			{#if category.subCategories.some((sub) => (activeInfusion?.tastingNotes?.filter((e) => e.category === category.name && e.subCategory === sub) ?? []).length > 0)}
				<div class="flex flex-col gap-2">
					<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>

					{#each category.subCategories as subCategory (subCategory)}
						{@const notes =
							activeInfusion?.tastingNotes?.filter(
								(e) => e.category === category.name && e.subCategory === subCategory
							) ?? []}
						{#if notes.length > 0}
							<TastingNoteDisplay
								name={subCategory}
								{notes}
								openModal={() => tastingNoteModal?.open(category.name, subCategory)}
							/>
						{/if}
					{/each}
				</div>
			{/if}
		{/each}
	{:else}
		<Button
			class="w-full btn-dash"
			label="Add your first tasting note"
			onclick={() => tastingNoteModal?.open()}
		/>
	{/if}
	<div class="flex flex-wrap items-center justify-between gap-2 md:justify-start">
		<Input
			placeholder="Temperature (°C)"
			step="0.5"
			type="number"
			bind:value={activeInfusion!.temperature}
		/>
		<Checkbox label="Rinse?" bind:value={activeInfusion!.rinse} />
		<Rating bind:value={activeInfusion!.rating} />
	</div>
{/snippet}

<TastingNoteModal bind:this={tastingNoteModal} {categories} infusion={activeInfusion} />
<TastingNoteModal
	bind:this={globalTastingNoteModal}
	categories={globalCategories}
	infusion={session}
/>

<div class="flex min-h-0 w-full flex-1 flex-col items-center justify-between gap-6">
	<div class="w-full px-2">
		<div>
			<b>{data.tea.name}</b>
			<span class="ml-2 text-sm text-base-content/80 italic">Session 48</span>
		</div>
		<div class="flex gap-2 text-sm">
			{#if session.weight || session.volume}
				<span
					>{session.weight ? session.weight + 'g' : ''}
					{session.weight && session.volume ? '/' : ''}
					{session.volume ? session.volume + 'ml' : ''}</span
				>
			{/if}
			{#if session.people}
				<span class="flex items-center"><User /> {session.people}</span>
			{/if}
			{#if session.location}
				<span class="flex items-center"><MapPin /> {session.location}</span>
			{/if}
		</div>
	</div>
	<div class="flex min-h-0 w-full flex-1 flex-col gap-6 overflow-x-hidden overflow-y-auto px-2">
		{#if activeTab.tab === 'infusion'}
			{@render infusionTab()}
		{:else if activeTab.tab === 'start'}
			<StartSettings {globalTastingNoteModal} bind:session />
		{:else if activeTab.tab === 'end'}
			<EndSettings bind:session />
		{/if}
	</div>
	{#if activeTab.tab === 'infusion'}
		{#key activeTab.index}
			<TimerBar {activeInfusion} bind:isTimerRunning />
		{/key}
	{/if}
	<SessionBottomBar disabled={isTimerRunning} bind:infusions={session.infusions} bind:activeTab />
</div>
