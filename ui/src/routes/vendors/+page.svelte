<script lang="ts">
	import { Leaf, MapPin, Pen, Scale, Trash } from '@lucide/svelte';
	const { data } = $props();
</script>

<div class="w-full p-8">
	{#each data.vendors as overview (overview.vendor!.name)}
		<div class="card mb-4 w-full bg-base-200 shadow">
			<div class="card-body">
				<div class="flex items-center justify-between">
					<div class="text-lg font-bold">{overview.vendor!.name}</div>
					<div>
						<button class="btn btn-square"><Pen /></button>
						<button class="btn btn-square bg-error"><Trash /></button>
					</div>
				</div>
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

					{#if overview.vendor!.locationDto}
						<div class="stat">
							<div class="stat-figure text-secondary">
								<MapPin class="icon-lg" />
							</div>
							<div class="stat-title">Ort</div>
							<div class="stat-value">{overview.vendor!.locationDto.city}</div>
							<div class="stat-desc">{overview.vendor!.locationDto.country}</div>
						</div>
					{/if}
				</div>
			</div>
		</div>
	{/each}
</div>
