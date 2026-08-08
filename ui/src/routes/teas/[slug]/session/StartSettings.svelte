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
		inputClass="w-full"
		placeholder="Weight (g)"
		step="0.01"
		type="number"
		bind:value={session.weight}
	/>

	<Input
		icon={Beaker}
		inputClass="w-full"
		placeholder="Volume (ml)"
		step="1"
		type="number"
		bind:value={session.volume}
	/>

	<Combobox
		class="col-span-full"
		icon={CookingPot}
		options={['Gongfu', 'Western', 'Grandpa', 'Coldbrew']}
		placeholder="Brewing Method"
		bind:value={session.brewingMethod}
	/>

	<div class="col-span-full w-full text-xs text-base-content/50 uppercase">Surrounding</div>
	<Input icon={User} inputClass="w-full" placeholder="People" bind:value={session.people} />
	<Input icon={MapPin} inputClass="w-full" placeholder="Location" bind:value={session.location} />
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
		class="w-full btn-dash"
		label="Add your first tasting note"
		onclick={() => globalTastingNoteModal?.open()}
	/>
{/if}
