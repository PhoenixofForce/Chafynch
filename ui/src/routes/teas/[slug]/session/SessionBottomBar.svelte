<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import { Flag, Leaf, Plus } from '@lucide/svelte';
	import { tick } from 'svelte';
	import type { Infusion, Tabs } from './types';

	let {
		disabled = false,
		infusions = $bindable([]),
		activeTab = $bindable()
	}: { disabled?: boolean; infusions: Infusion[]; activeTab: Tabs } = $props();

	let scrollable: HTMLDivElement;

	function setInfusion(index: number) {
		activeTab = {
			tab: 'infusion',
			index
		};
	}

	async function addInfusion() {
		infusions.push({
			startTime: new Date(),
			tastingNotes: {}
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
			{disabled}
			class="h-20 w-16 btn-ghost"
			icon={Leaf}
			onclick={() => (activeTab = { tab: 'start' })}
		/>
	</div>
	<div class="flex flex-1 gap-4 overflow-x-auto py-4" bind:this={scrollable}>
		{#each infusions as infusion, i (infusion.startTime)}
			<Button
				class="h-20 w-16 {activeTab.tab === 'infusion' && i == activeTab.index!
					? 'btn-primary'
					: 'btn-dash'}"
				onclick={() => setInfusion(i)}
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
		<Button
			{disabled}
			class="h-20 w-16 btn-ghost"
			icon={Flag}
			onclick={() => (activeTab = { tab: 'end' })}
		/>
	</div>
</div>
