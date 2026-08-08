<script lang="ts">
	import Combobox from '$lib/basics/Combobox.svelte';
	import Input from '$lib/basics/Input.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import { globalCategories, type Session } from './types';
	import TastingNoteModal from './TastingNoteModal.svelte';
	import Button from '$lib/basics/Button.svelte';
	import { Beaker, CookingPot, MapPin, Scale, User } from '@lucide/svelte';

	let {
		session = $bindable(),
		globalTastingNoteModal
	}: { session: Session; globalTastingNoteModal: ReturnType<typeof TastingNoteModal> } = $props();

	let hasNotes = $derived(
		Object.values(session?.tastingNotes ?? {}).some((notes) => notes.length > 0)
	);
</script>

<div class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2">
	<div class="col-span-full w-full text-xs text-base-content/50 uppercase">Brewing Parameter</div>

	<Input
		icon={Scale}
		type="number"
		inputClass="w-full"
		bind:value={session.weight}
		step="0.01"
		placeholder="Weight (g)"
	/>

	<Input
		icon={Beaker}
		type="number"
		inputClass="w-full"
		bind:value={session.volume}
		step="1"
		placeholder="Volume (ml)"
	/>

	<Combobox
		icon={CookingPot}
		class="col-span-full"
		bind:value={session.brewingMethod}
		placeholder="Brewing Method"
		options={['Gongfu', 'Western', 'Grandpa', 'Coldbrew']}
	/>

	<div class="col-span-full w-full text-xs text-base-content/50 uppercase">Surrounding</div>
	<Input icon={User} inputClass="w-full" bind:value={session.people} placeholder="People" />
	<Input icon={MapPin} inputClass="w-full" bind:value={session.location} placeholder="Location" />
</div>
{#if hasNotes}
	{#each globalCategories as category (category.name)}
		{#if category.subCategories.some((sub) => (session?.tastingNotes[category.name + '/' + sub] ?? []).length > 0)}
			<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>
		{/if}

		{#each category.subCategories as subCategory (subCategory)}
			{@const notes = session?.tastingNotes[category.name + '/' + subCategory] ?? []}
			{#if notes.length > 0}
				<TastingNoteDisplay
					name={subCategory}
					{notes}
					openModal={() => globalTastingNoteModal?.open(category.name, subCategory)}
				/>
			{/if}
		{/each}
	{/each}
{:else}
	<Button
		label="Add your first tasting note"
		class="w-full btn-dash"
		onclick={() => globalTastingNoteModal?.open()}
	/>
{/if}
