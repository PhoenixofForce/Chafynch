<script lang="ts">
	import Input from '$lib/basics/Input.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import { globalCategories } from './types';
	import TastingNoteModal from './TastingNoteModal.svelte';
	import Button from '$lib/basics/Button.svelte';
	import { Beaker, MapPin, Scale, User } from '@lucide/svelte';
	import type { SessionDto } from '$lib/api/gen/types';

	let {
		session = $bindable(),
		globalTastingNoteModal
	}: { session: SessionDto; globalTastingNoteModal: ReturnType<typeof TastingNoteModal> } =
		$props();

	let hasNotes = $derived((session.tastingNotes?.length ?? 0) > 0);
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

	<!-- forgotton
	<Combobox
		class="col-span-full"
		icon={CookingPot}
		options={['Gongfu', 'Western', 'Grandpa', 'Coldbrew']}
		placeholder="Brewing Method"
		bind:value={session.brewingMethod}
	/>
	 -->

	<div class="col-span-full w-full text-xs text-base-content/50 uppercase">Surrounding</div>
	<Input icon={User} inputClass="w-full" placeholder="People" bind:value={session.people} />
	<Input icon={MapPin} inputClass="w-full" placeholder="Location" bind:value={session.location} />
</div>
{#if hasNotes}
	{#each globalCategories as category (category.name)}
		{#if category.subCategories.some((sub) => (session?.tastingNotes?.filter((e) => e.category === category.name && e.subCategory === sub) ?? []).length > 0)}
			<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>
		{/if}

		{#each category.subCategories as subCategory (subCategory)}
			{@const notes =
				session?.tastingNotes?.filter(
					(e) => e.category === category.name && e.subCategory === subCategory
				) ?? []}
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
