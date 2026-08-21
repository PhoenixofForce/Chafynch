<script lang="ts">
	import CountryMap from '$lib/geo/CountryMap.svelte';
	import Button from '$lib/basics/Button.svelte';
	import Markdown from '$lib/basics/Markdown.svelte';
	import { Calendar, Coffee, Coins, Pen, Trash, Weight } from '@lucide/svelte';
	import { icons } from '$lib/basics/icons.js';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import { teaService } from '$lib/api/tea.service.js';
	import { toast } from '$lib/toast/toast.store.svelte.js';
	import { confirmation } from '$lib/confirmation/confirmation.store.svelte.js';
	import { sessionService } from '$lib/api/session.service.js';
	import Rating from '$lib/basics/Rating.svelte';

	let { data } = $props();
	let deleting = $state(false);

	async function deleteTea() {
		confirmation.show({
			title: 'Do you really want to delete tea?',
			confirm: {
				label: 'Delete',
				class: 'btn-error',
				onclick: () => {
					deleting = true;
					teaService
						.delete(data.tea.id!)
						.then(() => {
							toast.success(`Successfully deleted '${data.tea.name}'`);
							goto(resolve('/teas'));
							confirmation.hide();
						})
						.catch(() => undefined)
						.finally(() => {
							deleting = false;
						});
				}
			}
		});
	}

	async function startNewSession() {
		const newSession = await sessionService.create(data.tea.id!, {
			startTime: new Date().toISOString()
		});

		if (!newSession || !newSession.id) {
			toast.error('Could not create new session');
		}

		goto(
			resolve('/teas/[slug]/session/[sessionId]', {
				slug: data.tea.id + '',
				sessionId: newSession.id! + ''
			})
		);
	}
</script>

<div class="grid w-full max-w-full grid-cols-1 gap-8 md:grid-cols-[2fr_5fr] md:gap-12">
	<div class="md:col-start-2 md:row-start-1">{@render header()}</div>
	<div class="md:relative md:col-start-1 md:row-start-1">{@render country()}</div>

	<div class="card bg-base-200 px-6 py-4 text-base-content/70 shadow">{@render stats()}</div>
	<div>
		{@render description()}
	</div>

	<div class="md:col-span-2">{@render table()}</div>
</div>

{#snippet description()}
	<Markdown md={data.tea.descriptionMd ?? ''}>
		{#snippet before()}
			<h2>Description</h2>
		{/snippet}
	</Markdown>
{/snippet}

{#snippet header()}
	<div class="card bg-base-300 text-base-content">
		<div class="card-body">
			<div class="prose">
				<h1 class="mb-2 text-primary">{data.tea.name}</h1>
			</div>
			<div class="flex flex-col gap-4 md:flex-row md:gap-2">
				<div>
					{#if data.tea.teaType}
						<div class="badge badge-primary">
							<icons.teaType />
							{data.tea.teaType}
						</div>
					{/if}
					{#if data.tea.cultivar}
						<div class="badge badge-primary">
							<icons.cultivar />
							{data.tea.cultivar}
						</div>
					{/if}
					{#if data.tea.vendor}
						<div class="badge badge-primary">
							<icons.vendor />
							{data.tea.vendor}
						</div>
					{/if}
				</div>

				<div class="rating">
					{#each { length: 5 }, i (i)}
						<input
							name="rating"
							class="mask bg-primary mask-star"
							aria-label="{i} star"
							checked={i + 1 === (data.tea.rating ?? 0)}
							disabled
							type="radio"
						/>
					{/each}
				</div>
			</div>

			<div class="stats mt-6 stats-vertical bg-base-100 shadow md:stats-horizontal">
				{#if data.tea.harvestYear || data.tea.harvestLabel}
					<div class="stat">
						<div class="stat-figure text-secondary">
							<Calendar />
						</div>
						<div class="stat-title">Harvest</div>
						<div class="stat-value">{data.tea.harvestYear ?? data.tea.harvestLabel}</div>
						<div class="stat-desc">{data.tea.harvestYear ? data.tea.harvestLabel : ''}</div>
					</div>
				{/if}

				{#if data.tea.weightGrams && data.tea.weightGrams > 0}
					<div class="stat">
						<div class="stat-figure text-secondary">
							<Coins />
						</div>
						<div class="stat-title">Average Price</div>
						<div class="stat-value">
							{(data.tea.price ?? 0) / data.tea.weightGrams}
						</div>
						<div class="stat-desc">$ / g</div>
					</div>
				{/if}

				<div class="stat">
					<div class="stat-figure text-secondary">
						<Weight />
					</div>
					<div class="stat-title">Inventory</div>
					<div class="stat-value">20 g</div>
					<div class="stat-desc">In the Sideboard</div>
				</div>
			</div>

			<div class="my-auto flex items-center justify-around gap-4 md:justify-end">
				{#if data.tea.website}
					<a
						class="link"
						href={data.tea.website}
						rel="external noopener noreferrer"
						target="_blank"
					>
						Buy more
					</a>
				{/if}

				<Button
					class="btn-ghost"
					disabled={deleting}
					icon={Pen}
					label="Edit"
					onclick={() => goto(resolve('/teas/[slug]/edit', { slug: data.tea.id + '' }))}
				/>
				<Button
					class="btn-ghost btn-error"
					icon={Trash}
					label="Delete"
					loading={deleting}
					onclick={deleteTea}
				/>
			</div>
		</div>
	</div>
{/snippet}

{#snippet table()}
	<div class="flex flex-col items-center gap-4 md:items-end">
		<div class="prose mb-3 flex min-w-full items-center justify-between">
			<h2 class="mb-0">Session Logs</h2>
			<Button class="btn-primary" icon={Coffee} label="Drink now" onclick={startNewSession} />
		</div>
		{#if data.sessions && data.sessions.length}
			<div class="w-full overflow-x-auto">
				<table class="table table-zebra table-xs">
					<thead>
						<tr>
							<th></th>
							<th>Tea</th>
							<th>Rating</th>
							<th>Infusions</th>
							<th>g/ml</th>
							<th>Location</th>
							<th>People</th>
							<th>Date</th>
						</tr>
					</thead>
					<tbody>
						{#each data.sessions as session (session.id)}
							<tr
								class="cursor-pointer"
								onclick={() =>
									goto(
										resolve('/teas/[slug]/session/[sessionId]', {
											slug: data.tea.id! + '',
											sessionId: session.id! + ''
										})
									)}
							>
								<th>{session.id}</th>
								<td>{data.tea.name}</td>
								<td><Rating class="rating-sm" readonly value={session.displayRating} /></td>
								<td>{session.infusions?.length} infusions</td>
								<td>{session.weight}g / {session.volume}ml</td>
								<td>{session.location}</td>
								<td>{session.people}</td>
								<td>{session.startTime}</td>
							</tr>
						{/each}
					</tbody>
				</table>
			</div>
			<div class="join mt-4">
				<button class="btn join-item">1</button>
				<button class="btn btn-active join-item">2</button>
				<button class="btn join-item">3</button>
				<button class="btn join-item">4</button>
			</div>
		{/if}
	</div>
{/snippet}

{#snippet country()}
	<div class="flex h-full flex-col md:absolute md:inset-0">
		<div class="min-h-0 flex-1">
			<CountryMap
				country={data.tea.originCountry ?? ''}
				markerLat={data.tea.originLatitude}
				markerLon={data.tea.originLongitude}
				showNeighbors={false}
			/>
		</div>

		<div>
			<div class="badge badge-primary">{data.tea.originCountry}</div>
		</div>
	</div>
{/snippet}

{#snippet stats()}
	<div class="flex flex-col gap-4 text-xs">
		{#if data.tea.tastingNotes?.length}
			<b>Top Tasting Notes</b>
			<div class="flex flex-wrap gap-2">
				{#each data.tea.tastingNotes as tastingNote (tastingNote)}
					<div class="badge badge-accent">
						{tastingNote}
					</div>
				{/each}
			</div>
		{/if}

		<div>more info coming soon</div>
	</div>
{/snippet}
