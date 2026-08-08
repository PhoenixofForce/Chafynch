<script lang="ts">
	import type { VendorDto } from '$lib/api/gen/types.js';
	import BasicEntityCard from '$lib/crud/BasicEntityCard.svelte';
	import { createEditor } from '$lib/crud/editable.svelte.js';
	import { Leaf, MapPin, Plus, Scale } from '@lucide/svelte';
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { onMount } from 'svelte';
	import Combobox from '$lib/basics/Combobox.svelte';
	import { vendorService } from '$lib/api/vendor.service.js';
	import { toast } from '$lib/toast/toast.store.svelte.js';

	const { data } = $props();
	const editor = createEditor<VendorDto>();

	function create() {
		const draft: VendorDto = {
			id: -1,
			name: '',
			locationDto: { country: '', province: '', city: '' }
		};
		editor.create(draft);
	}

	async function onSave(vendor: VendorDto, isNew: boolean) {
		if (isNew) {
			await vendorService.create(vendor);
			return toast.success(`Successfully created '${vendor.name}'`);
		}

		await vendorService.update(vendor);
		return toast.success(`Successfully updated '${vendor.name}'`);
	}

	async function onDelete(vendor: VendorDto) {
		await vendorService.delete(vendor.id);
		toast.success(`Successfully deleted '${vendor.name}'`);
	}

	let countryNames: string[] = $state([]);
	onMount(() => {
		fetch('/countries/index.json').then(async (res) => {
			if (res.ok) {
				const entries: { code: string; name: string }[] = await res.json();
				countryNames = entries.map((e) => e.name);
			}
		});
	});
</script>

{#snippet editTitle(draft: VendorDto)}
	<div class="mr-4 flex-1">
		<Input
			hint="Name is required"
			inputClass="w-full"
			placeholder="Name*"
			required
			bind:value={draft.name}
		/>
	</div>
{/snippet}

{#snippet editHeader(draft: VendorDto)}
	<div>
		<Input
			class="mb-2"
			hint="Must be a valid URL"
			inputClass="w-full"
			pattern="(http(s?):\/\/)([a-zA-Z0-9-]+\.)+[a-zA-Z]+(\/.*)?"
			placeholder="Website"
			type="url"
			bind:value={draft.website}
		/>
	</div>

	<div class="grid grid-cols-3 gap-2">
		<Combobox
			options={countryNames}
			placeholder="Country"
			bind:value={draft.locationDto!.country}
		/>

		<Input inputClass="w-full" placeholder="Provinz" bind:value={draft.locationDto!.province} />
		<Input inputClass="w-full" placeholder="Stadt" bind:value={draft.locationDto!.city} />
	</div>
{/snippet}

<div class="flex w-full flex-col gap-8 p-8">
	<div class="prose">
		<h2>Manage Vendors</h2>
	</div>

	{#if !editor.isNew}
		<Button
			class="btn btn-dash btn-primary"
			disabled={editor.editingAny()}
			icon={Plus}
			label="Add Vendor"
			onclick={create}
		/>
	{/if}

	{#if editor.isNew}
		<BasicEntityCard {editHeader} {editTitle} {editor} entity={editor.draft!} {onDelete} {onSave}>
			{#snippet title()}
				<div class="text-lg font-bold">New Vendor</div>
			{/snippet}
		</BasicEntityCard>
	{/if}

	{#each data.vendors as overview (overview.vendor.id)}
		<BasicEntityCard {editHeader} {editTitle} {editor} entity={overview.vendor} {onDelete} {onSave}>
			{#snippet title()}
				<div class="m-0 text-lg font-bold">{overview.vendor.name}</div>
			{/snippet}

			{#snippet header()}
				{#if overview.vendor.website}
					<div>
						<a href={overview.vendor.website} rel="external noopener noreferrer" target="_blank">
							Visit Shop
						</a>
					</div>
				{/if}
			{/snippet}

			{#snippet body()}
				<div class="stats stats-vertical bg-base-100 shadow md:stats-horizontal">
					<div class="stat">
						<div class="stat-figure text-secondary">
							<Leaf class="icon-lg" />
						</div>
						<div class="stat-title">Total Teas</div>
						<div class="stat-value">{overview.teas}</div>
						<div class="stat-desc"></div>
					</div>

					<div class="stat">
						<div class="stat-figure text-secondary">
							<Scale class="icon-lg" strokeWidth="2" />
						</div>
						<div class="stat-title">Average Price per Gram</div>
						<div class="stat-value">{overview.averagePricePerGram}</div>
						<div class="stat-desc"></div>
					</div>

					{#if overview.vendor.locationDto?.country}
						<div class="stat">
							<div class="stat-figure text-secondary">
								<MapPin class="icon-lg" />
							</div>
							<div class="stat-title">Ort</div>
							<div class="stat-value">{overview.vendor.locationDto.city}</div>
							<div class="stat-desc">{overview.vendor.locationDto.country}</div>
						</div>
					{/if}
				</div>
			{/snippet}
		</BasicEntityCard>
	{/each}
</div>
