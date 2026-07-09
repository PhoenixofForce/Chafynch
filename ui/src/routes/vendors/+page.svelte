<script lang="ts">
	import { api, wrapApi } from '$lib/api/client.js';
	import type { VendorDto } from '$lib/api/types.js';
	import BasicEntityCard from '$lib/components/BasicEntityCard.svelte';
	import SearchableSelect from '$lib/components/SearchableSelect.svelte';
	import { createEditor } from '$lib/data/editable.svelte.js';
	import { Leaf, MapPin, Plus, Scale } from '@lucide/svelte';
	import Button from '$lib/components/ui/Button.svelte';
	import Input from '$lib/components/ui/Input.svelte';
	import { onMount } from 'svelte';

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
			return wrapApi(() => api.POST('/api/vendors', { body: vendor }), {
				success: `Successfully created vendor '${vendor.name}'`
			});
		}

		return wrapApi(
			() => api.PUT('/api/vendors/{id}', { body: vendor, params: { path: { id: vendor.id } } }),
			{
				success: `Successfully updated vendor '${vendor.name}'`
			}
		);
	}

	async function onDelete(vendor: VendorDto) {
		return wrapApi(() => api.DELETE('/api/vendors/{id}', { params: { path: { id: vendor.id } } }), {
			success: `Successfully deleted vendor '${vendor.name}'`
		});
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
	<Input
		required
		placeholder="Name*"
		bind:value={draft.name}
		hint="Name is required"
		class="mb-2"
	/>
{/snippet}

{#snippet editHeader(draft: VendorDto)}
	<div>
		<Input
			type="url"
			pattern="(http(s?):\/\/)([a-zA-Z0-9-]+\.)+[a-zA-Z]+(\/.*)?"
			placeholder="Website"
			bind:value={draft.website}
			hint="Must be a valid URL"
			class="mb-2"
		/>
	</div>

	<div class="grid grid-cols-3 gap-2">
		<SearchableSelect
			placeholder="Land"
			options={countryNames}
			bind:value={draft.locationDto!.country}
		/>
		<Input placeholder="Provinz" bind:value={draft.locationDto!.province} />
		<Input placeholder="Stadt" bind:value={draft.locationDto!.city} />
	</div>
{/snippet}

<div class="flex w-full flex-col gap-8 p-8">
	{#if !editor.isNew}
		<Button
			class="btn btn-dash btn-primary"
			onclick={create}
			label="Add Vendor"
			icon={Plus}
			disabled={editor.editingAny()}
		/>
	{/if}

	{#if editor.isNew}
		<BasicEntityCard entity={editor.draft!} {editor} {onSave} {onDelete} {editTitle} {editHeader}>
			{#snippet title()}
				<div class="text-lg font-bold">New Vendor</div>
			{/snippet}
		</BasicEntityCard>
	{/if}

	{#each data.vendors as overview (overview.vendor.id)}
		<BasicEntityCard entity={overview.vendor} {editor} {onSave} {onDelete} {editTitle} {editHeader}>
			{#snippet title()}
				<div class="m-0 text-lg font-bold">{overview.vendor.name}</div>
			{/snippet}

			{#snippet header()}
				{#if overview.vendor.website}
					<div>
						<a href={overview.vendor.website} rel="external" target="_blank"> Visit Shop </a>
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
