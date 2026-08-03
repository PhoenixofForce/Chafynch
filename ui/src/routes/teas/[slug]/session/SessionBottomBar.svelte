<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import { Flag, Leaf, Plus } from '@lucide/svelte';
	import { tick } from 'svelte';
	import type { Infusion } from './types';

	let {
		disabled = false,
		infusions = $bindable([]),
		activeInfusion = $bindable(0)
	}: { disabled?: boolean; infusions: Infusion[]; activeInfusion: number } = $props();

	let scrollable: HTMLDivElement;

	async function addInfusion() {
		infusions.push({
			startTime: new Date(),
			tastingNotes: {}
		});

		activeInfusion = infusions.length - 1;
		await tick();
		scrollable.scrollTo({
			left: scrollable.scrollWidth,
			behavior: 'instant'
		});
	}
</script>

<div class="flex w-full gap-6">
	<div class="py-4">
		<Button {disabled} class="h-20 w-16 btn-ghost" icon={Leaf} />
	</div>
	<div class="flex flex-1 gap-4 overflow-x-auto py-4" bind:this={scrollable}>
		{#each infusions as infusion, i (infusion.startTime)}
			<Button
				class="h-20 w-16 {i == activeInfusion ? 'btn-primary' : 'btn-dash'}"
				onclick={() => (activeInfusion = i)}
				{disabled}
			>
				<div class="flex flex-col">
					<b>{infusion.isRinse ? '~' : i + 1}</b>
					<span class="text-xs text-neutral/80">
						{infusion.infusionTime ? infusion.infusionTime.toFixed(2) + 's' : ''}
					</span>
					<span class="text-xs text-neutral/80">
						{'★'.repeat(infusion.rating ?? 0)}
					</span>
				</div>
			</Button>
		{/each}
		<Button {disabled} class="h-20 w-16 btn-dash" icon={Plus} onclick={addInfusion} />
	</div>

	<div class="py-4">
		<Button {disabled} class="h-20 w-16 btn-ghost" icon={Flag} />
	</div>
</div>
