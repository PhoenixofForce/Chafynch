<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Checkbox from '$lib/basics/Checkbox.svelte';
	import Input from '$lib/basics/Input.svelte';
	import Rating from '$lib/basics/Rating.svelte';
	import { MapPin, User } from '@lucide/svelte';
	import SessionBottomBar from './SessionBottomBar.svelte';
	import StartSetting from './StartSetting.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import TastingNoteModal from './TastingNoteModal.svelte';
	import TimerBar from './TimerBar.svelte';
	import { categories, globalCategories, type Session, type Tabs } from './types';

	let { data } = $props();

	let activeTab = $state<Tabs>({ tab: 'start' });
	let sessions = $state<Session>({
		infusions: [
			{
				startTime: new Date(),
				infusionTime: 24,
				rating: 3,
				tastingNotes: {
					'Eye/Wet Leaf': ['Sweet', 'Smooth']
				}
			}
		],
		tastingNotes: {}
	});

	let activeInfusion = $derived(
		activeTab.tab === 'infusion' ? sessions.infusions.at(activeTab.index) : undefined
	);
	let isTimerRunning = $state(false);

	let tastingNoteModal = $state<ReturnType<typeof TastingNoteModal>>();
	let globalTastingNoteModal = $state<ReturnType<typeof TastingNoteModal>>();
	let hasNotes = $derived(
		Object.values(activeInfusion?.tastingNotes ?? {}).some((notes) => notes.length > 0)
	);
</script>

{#snippet infusionTab()}
	{#if hasNotes}
		{#each categories as category (category.name)}
			{#if category.subCategories.some((sub) => (activeInfusion?.tastingNotes[category.name + '/' + sub] ?? []).length > 0)}
				<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>
			{/if}

			{#each category.subCategories as subCategory (subCategory)}
				{@const notes = activeInfusion?.tastingNotes[category.name + '/' + subCategory] ?? []}
				{#if notes.length > 0}
					<TastingNoteDisplay
						name={subCategory}
						{notes}
						openModal={() => tastingNoteModal?.open(category.name, subCategory)}
					/>
				{/if}
			{/each}
		{/each}
	{:else}
		<Button
			class="w-full btn-dash"
			label="Add your first tasting note"
			onclick={() => tastingNoteModal?.open()}
		/>
	{/if}
	<div class="flex flex-wrap items-center justify-between gap-2 md:justify-start">
		<Input placeholder="Temperature (°C)" step="0.5" type="number" />
		<Checkbox label="Rinse?" bind:value={activeInfusion!.isRinse} />
		<Rating bind:value={activeInfusion!.rating} />
	</div>
{/snippet}

<TastingNoteModal bind:this={tastingNoteModal} {categories} infusion={activeInfusion} />
<TastingNoteModal
	bind:this={globalTastingNoteModal}
	categories={globalCategories}
	infusion={sessions}
/>

<div class="flex min-h-0 w-full flex-1 flex-col items-center justify-between gap-6">
	<div class="w-full px-2">
		<div>
			<b>{data.tea.name}</b>
			<span class="ml-2 text-sm text-base-content/80 italic">Session 48</span>
		</div>
		<div class="flex gap-2 text-sm">
			{#if sessions.weight || sessions.volume}
				<span
					>{sessions.weight ? sessions.weight + 'g' : ''}
					{sessions.weight && sessions.volume ? '/' : ''}
					{sessions.volume ? sessions.volume + 'ml' : ''}</span
				>
			{/if}
			{#if sessions.people}
				<span class="flex items-center"><User /> {sessions.people}</span>
			{/if}
			{#if sessions.location}
				<span class="flex items-center"><MapPin /> {sessions.location}</span>
			{/if}
		</div>
	</div>
	<div class="flex min-h-0 w-full flex-1 flex-col gap-2 overflow-x-hidden overflow-y-auto px-2">
		{#if activeTab.tab === 'infusion'}
			{@render infusionTab()}
		{:else if activeTab.tab === 'start'}
			<StartSetting {globalTastingNoteModal} bind:session={sessions} />
		{:else if activeTab.tab === 'end'}
			<span>end settings here</span>
		{/if}
	</div>
	{#if activeTab.tab === 'infusion'}
		<TimerBar {activeInfusion} bind:isTimerRunning />
	{/if}
	<SessionBottomBar disabled={isTimerRunning} bind:infusions={sessions.infusions} bind:activeTab />
</div>
