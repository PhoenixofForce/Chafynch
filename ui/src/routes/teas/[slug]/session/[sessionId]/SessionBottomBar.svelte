<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import { Flag, Leaf, Plus } from '@lucide/svelte';
	import { tick } from 'svelte';
	import type { Tabs } from './types';
	import type { InfusionDto } from '$lib/api/gen/types';

	let {
		disabled = false,
		infusions = $bindable([]),
		activeTab = $bindable()
	}: { disabled?: boolean; infusions?: InfusionDto[]; activeTab: Tabs } = $props();

	let scrollable: HTMLDivElement;

	function setInfusion(index: number) {
		activeTab = {
			tab: 'infusion',
			index
		};
	}

	async function addInfusion() {
		infusions.push({
			startTime: new Date().toISOString(),
			tastingNotes: []
		});

		setInfusion(infusions.length - 1);
		await tick();
		scrollable.scrollTo({
			left: scrollable.scrollWidth,
			behavior: 'instant'
		});
	}
</script>

<div class="flex w-full gap-6">
	<div class="py-4">
		<Button
			class="h-20 w-16 {activeTab.tab === 'start' ? 'btn-primary' : 'btn-ghost'}"
			{disabled}
			icon={Leaf}
			onclick={() => (activeTab = { tab: 'start' })}
		/>
	</div>
	<div bind:this={scrollable} class="flex flex-1 gap-4 overflow-x-auto py-4">
		{#each infusions as infusion, i (infusion.startTime)}
			<Button
				class="h-20 w-16 {activeTab.tab === 'infusion' && i == activeTab.index!
					? 'btn-primary'
					: 'btn-dash'}"
				{disabled}
				onclick={() => setInfusion(i)}
			>
				<div class="flex flex-col">
					<b>{infusion.rinse ? '~' : i + 1}</b>
					<span class="text-xs text-neutral/80">
						{infusion.infusionTime ? infusion.infusionTime.toFixed(2) + 's' : ''}
					</span>
					<span class="text-xs text-neutral/80">
						{'★'.repeat(infusion.rating ?? 0)}
					</span>
				</div>
			</Button>
		{/each}
		<Button class="h-20 w-16 btn-dash" {disabled} icon={Plus} onclick={addInfusion} />
	</div>

	<div class="py-4">
		<Button
			class="h-20 w-16 {activeTab.tab === 'end' ? 'btn-primary' : 'btn-ghost'}"
			{disabled}
			icon={Flag}
			onclick={() => (activeTab = { tab: 'end' })}
		/>
	</div>
</div>
