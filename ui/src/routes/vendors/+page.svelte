<script lang="ts">
	import { api, wrapApi } from '$lib/api/client.js';
	import type { VendorDto } from '$lib/api/types.js';
	import BasicEntityCard from '$lib/components/BasicEntityCard.svelte';
	import { createEditor } from '$lib/data/editable.svelte.js';
	import { Globe, Leaf, MapPin, Scale } from '@lucide/svelte';
	const { data } = $props();

	const editor = createEditor<VendorDto>();

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
</script>

<div class="w-full p-8">
	{#each data.vendors as overview (overview.vendor.id)}
		<BasicEntityCard entity={overview.vendor} {editor} {onSave} {onDelete}>
			{#snippet title()}
				<div class="text-lg font-bold">{overview.vendor.name}</div>
			{/snippet}

			{#snippet editTitle(draft: VendorDto)}
				<div class="text-lg font-bold">{draft.name}</div>
			{/snippet}

			{#snippet header()}
				{#if overview.vendor.website}
					<div>
						<a href={overview.vendor.website} rel="external" target="_blank"> Visit Shop </a>
					</div>
				{/if}
			{/snippet}

			{#snippet editHeader(draft: VendorDto)}
				<label class="validator input">
					<Globe class="opacity-60" />
					<input
						type="url"
						class="validator"
						placeholder="Enter Shop URL"
						bind:value={draft.website}
						pattern="^(https?://)?([a-zA-Z0-9]([a-zA-Z0-9-].*[a-zA-Z0-9])?.)+[a-zA-Z].*$"
					/>
				</label>
				<p class="validator-hint mt-0">Must be valid URL</p>

				<label class="input">
					<Globe class="opacity-60" />
					<input
						class=""
						type="text"
						placeholder="Enter Shop URL"
						bind:value={draft.locationDto!.country}
					/>
				</label>
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
