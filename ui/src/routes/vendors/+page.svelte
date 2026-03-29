<script lang="ts">
	import { Delete, Leaf, MapPin, Pen, Scale } from '@lucide/svelte';

	const vendors = [
		{ name: 'Yoshien', teas: '2', address: { city: 'Berlin', country: 'Germany' } },
		{ name: 'Tea Kontor Kiel', teas: 0, address: { city: 'Kiel', country: 'Germany' } }
	];
</script>

<div class="w-full p-8">
	{#each vendors as vendor (vendor.name)}
		<div class="card mb-4 w-full bg-base-200 shadow">
			<div class="card-body">
				<div class="flex items-center justify-between">
					<div class="text-lg font-bold">{vendor.name}</div>
					<div>
						<button class="btn btn-square"><Pen /></button>
						<button class="btn btn-square bg-error"><Delete /></button>
					</div>
				</div>
				<div class="stats bg-base-100 shadow">
					<div class="stat">
						<div class="stat-figure text-secondary">
							<Leaf class="icon-lg" />
						</div>
						<div class="stat-title">Total Teas</div>
						<div class="stat-value">{vendor.teas}</div>
						<div class="stat-desc"></div>
					</div>

					<div class="stat">
						<div class="stat-figure text-secondary">
							<Scale class="icon-lg" strokeWidth="2" />
						</div>
						<div class="stat-title">Average Price per Gram</div>
						<div class="stat-value">32ct</div>
						<div class="stat-desc"></div>
					</div>

					<div class="stat">
						<div class="stat-figure text-secondary">
							<MapPin class="icon-lg" />
						</div>
						<div class="stat-title">Ort</div>
						<div class="stat-value">{vendor.address.city}</div>
						<div class="stat-desc">{vendor.address.country}</div>
					</div>
				</div>
			</div>
		</div>
	{/each}
</div>
