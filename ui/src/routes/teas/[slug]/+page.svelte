<script lang="ts">
	import CountryMap from '$lib/components/CountryMap.svelte';
	import Button from '$lib/components/ui/Button.svelte';
	import Markdown from '$lib/components/ui/Markdown.svelte';
	import { Calendar, Coffee, Coins, Pen, Trash, Weight } from '@lucide/svelte';

	let { data } = $props();

	const md = `

This is one of these amazing teas I found during my trip to Pinglin during 2023 spring.

I was focused mainly on fresh Baozhongs made from old cultivars… but as you know me, I was asking
every farmer about old teas in their storages.

1970’s Dry Stored Aged Pinglin Oolong is 50 years old tea what makes it the oldest tea in our offer.

It’s of course huge taste and aroma experience, but this tea carries lots about history of tea in
that area. You can easily notice the tea is rolled in different way with quite lots of stems. You
can see that old rolling machines were making different results comparing to modern teas.

When tasting this amazing oolong you will find notes of fresh tobacco, dark cane sugar, wood, incenses
and surprising nuances of candied star fruit and ginseng.

Aftertaste stays with drinker really long time.

Tea is really smooth, balanced and sweet with well developed aged character.

Warming and deeply relaxing experience.
	`;
</script>

<div class="grid w-full grid-cols-1 gap-8 md:grid-cols-[2fr_5fr] md:gap-12">
	<div class="md:col-start-2 md:row-start-1">{@render header()}</div>
	<div class="md:relative md:col-start-1 md:row-start-1">{@render country()}</div>

	<div class="card bg-base-200 px-6 py-8 text-base-content/70 shadow">{@render stats()}</div>
	<div>
		{@render description()}
	</div>

	<div class="md:col-span-2">{@render table()}</div>
</div>

{#snippet description()}
	<Markdown {md}>
		{#snippet after()}
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
					<div class="badge badge-primary">{data.tea.teaType}</div>
					<div class="badge badge-primary">{data.tea.cultivar}</div>
					<div class="badge badge-primary">{data.tea.vendor}</div>
				</div>

				<div class="rating">
					<input
						type="radio"
						name="rating-1"
						class="mask bg-primary mask-star"
						aria-label="1 star"
					/>
					<input
						type="radio"
						name="rating-1"
						class="mask bg-primary mask-star"
						aria-label="2 star"
						checked="checked"
					/>
					<input
						type="radio"
						name="rating-1"
						class="mask bg-primary mask-star"
						aria-label="3 star"
					/>
					<input
						type="radio"
						name="rating-1"
						class="mask bg-primary mask-star"
						aria-label="4 star"
					/>
					<input
						type="radio"
						name="rating-1"
						class="mask bg-primary mask-star"
						aria-label="5 star"
					/>
				</div>
			</div>

			<div class="stats mt-6 stats-vertical bg-base-100 shadow md:stats-horizontal">
				<div class="stat">
					<div class="stat-figure text-secondary">
						<Calendar />
					</div>
					<div class="stat-title">Harvest</div>
					<div class="stat-value">2026</div>
					<div class="stat-desc">First flush</div>
				</div>

				<div class="stat">
					<div class="stat-figure text-secondary">
						<Coins />
					</div>
					<div class="stat-title">Average Price</div>
					<div class="stat-value">1.20</div>
					<div class="stat-desc">$ / g</div>
				</div>

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
				<a class="link"> Buy more </a>

				<Button class="btn-ghost" label="Edit" icon={Pen} />
				<Button class="btn-ghost btn-error" label="Delete" icon={Trash} />
			</div>
		</div>
	</div>
{/snippet}

{#snippet table()}
	<div class="flex flex-col items-center gap-4 md:items-end">
		<div class="prose mb-3 flex min-w-full items-center justify-between">
			<h2 class="mb-0">Session Logs</h2>
			<Button class="btn-primary" label="Drink now" icon={Coffee} />
		</div>
		<div class="w-full overflow-x-auto">
			<table class="table table-zebra table-xs">
				<thead>
					<tr>
						<th></th>
						<th>Name</th>
						<th>Job</th>
						<th>company</th>
						<th>location</th>
						<th>Last Login</th>
						<th>Favorite Color</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>1</th>
						<td>Cy Ganderton</td>
						<td>Quality Control Specialist</td>
						<td>Littel, Schaden and Vandervort</td>
						<td>Canada</td>
						<td>12/16/2020</td>
						<td>Blue</td>
					</tr>
					<tr>
						<th>2</th>
						<td>Hart Hagerty</td>
						<td>Desktop Support Technician</td>
						<td>Zemlak, Daniel and Leannon</td>
						<td>United States</td>
						<td>12/5/2020</td>
						<td>Purple</td>
					</tr>
					<tr>
						<th>3</th>
						<td>Brice Swyre</td>
						<td>Tax Accountant</td>
						<td>Carroll Group</td>
						<td>China</td>
						<td>8/15/2020</td>
						<td>Red</td>
					</tr>
					<tr>
						<th>4</th>
						<td>Marjy Ferencz</td>
						<td>Office Assistant I</td>
						<td>Rowe-Schoen</td>
						<td>Russia</td>
						<td>3/25/2021</td>
						<td>Crimson</td>
					</tr>
					<tr>
						<th>5</th>
						<td>Yancy Tear</td>
						<td>Community Outreach Specialist</td>
						<td>Wyman-Ledner</td>
						<td>Brazil</td>
						<td>5/22/2020</td>
						<td>Indigo</td>
					</tr>
					<tr>
						<th>6</th>
						<td>Irma Vasilik</td>
						<td>Editor</td>
						<td>Wiza, Bins and Emard</td>
						<td>Venezuela</td>
						<td>12/8/2020</td>
						<td>Purple</td>
					</tr>
					<tr>
						<th>7</th>
						<td>Meghann Durtnal</td>
						<td>Staff Accountant IV</td>
						<td>Schuster-Schimmel</td>
						<td>Philippines</td>
						<td>2/17/2021</td>
						<td>Yellow</td>
					</tr>
					<tr>
						<th>8</th>
						<td>Sammy Seston</td>
						<td>Accountant I</td>
						<td>O'Hara, Welch and Keebler</td>
						<td>Indonesia</td>
						<td>5/23/2020</td>
						<td>Crimson</td>
					</tr>
					<tr>
						<th>9</th>
						<td>Lesya Tinham</td>
						<td>Safety Technician IV</td>
						<td>Turner-Kuhlman</td>
						<td>Philippines</td>
						<td>2/21/2021</td>
						<td>Maroon</td>
					</tr>
					<tr>
						<th>10</th>
						<td>Zaneta Tewkesbury</td>
						<td>VP Marketing</td>
						<td>Sauer LLC</td>
						<td>Chad</td>
						<td>6/23/2020</td>
						<td>Green</td>
					</tr>
					<tr>
						<th>11</th>
						<td>Andy Tipple</td>
						<td>Librarian</td>
						<td>Hilpert Group</td>
						<td>Poland</td>
						<td>7/9/2020</td>
						<td>Indigo</td>
					</tr>
					<tr>
						<th>12</th>
						<td>Sophi Biles</td>
						<td>Recruiting Manager</td>
						<td>Gutmann Inc</td>
						<td>Indonesia</td>
						<td>2/12/2021</td>
						<td>Maroon</td>
					</tr>
					<tr>
						<th>13</th>
						<td>Florida Garces</td>
						<td>Web Developer IV</td>
						<td>Gaylord, Pacocha and Baumbach</td>
						<td>Poland</td>
						<td>5/31/2020</td>
						<td>Purple</td>
					</tr>
					<tr>
						<th>14</th>
						<td>Maribeth Popping</td>
						<td>Analyst Programmer</td>
						<td>Deckow-Pouros</td>
						<td>Portugal</td>
						<td>4/27/2021</td>
						<td>Aquamarine</td>
					</tr>
					<tr>
						<th>15</th>
						<td>Moritz Dryburgh</td>
						<td>Dental Hygienist</td>
						<td>Schiller, Cole and Hackett</td>
						<td>Sri Lanka</td>
						<td>8/8/2020</td>
						<td>Crimson</td>
					</tr>
					<tr>
						<th>16</th>
						<td>Reid Semiras</td>
						<td>Teacher</td>
						<td>Sporer, Sipes and Rogahn</td>
						<td>Poland</td>
						<td>7/30/2020</td>
						<td>Green</td>
					</tr>
					<tr>
						<th>17</th>
						<td>Alec Lethby</td>
						<td>Teacher</td>
						<td>Reichel, Glover and Hamill</td>
						<td>China</td>
						<td>2/28/2021</td>
						<td>Khaki</td>
					</tr>
					<tr>
						<th>18</th>
						<td>Aland Wilber</td>
						<td>Quality Control Specialist</td>
						<td>Kshlerin, Rogahn and Swaniawski</td>
						<td>Czech Republic</td>
						<td>9/29/2020</td>
						<td>Purple</td>
					</tr>
					<tr>
						<th>19</th>
						<td>Teddie Duerden</td>
						<td>Staff Accountant III</td>
						<td>Pouros, Ullrich and Windler</td>
						<td>France</td>
						<td>10/27/2020</td>
						<td>Aquamarine</td>
					</tr>
					<tr>
						<th>20</th>
						<td>Lorelei Blackstone</td>
						<td>Data Coordiator</td>
						<td>Witting, Kutch and Greenfelder</td>
						<td>Kazakhstan</td>
						<td>6/3/2020</td>
						<td>Red</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="join mt-4">
			<button class="btn join-item">1</button>
			<button class="btn btn-active join-item">2</button>
			<button class="btn join-item">3</button>
			<button class="btn join-item">4</button>
		</div>
	</div>
{/snippet}

{#snippet country()}
	<div class="flex h-full flex-col md:absolute md:inset-0">
		<div class="min-h-0 flex-1">
			<CountryMap
				country={data.tea.originCountry ?? ''}
				markerLon={data.tea.originLongitude}
				markerLat={data.tea.originLatitude}
				showNeighbors={false}
			/>
		</div>

		<div>
			<div class="badge badge-primary">{data.tea.originCountry}</div>
		</div>
	</div>
{/snippet}

{#snippet stats()}
	<div class="flex flex-col gap-2 text-xs">
		<b>Character</b>
		<p>
			Als Cold Brew: besonders edelsüß, zugleich angenehm edelbitter, extrem erfrischend, volles
			Umami, sehr fruchtig, wunderbare Noten nach Melone, Gurke und Zitrone, äußerst vollmundig
			(Fukamushi) und aromatisch, typischer kräftiger Chiran-Charakter
		</p>
		<b>Teefarm</b>
		<p>Nuruki Seicha, renommierte Bio Teefarm im beliebten Terroir Chiran</p>
		<b>Terroir</b>
		<p>Chiran (Region), Kagoshima (Präf.), Japan</p>
		<b>Cultivar</b>
		<p>Yutakamidori (100%)</p>
		<b>Ernte</b>
		<p>1. Ernte 2026 (Ichibancha), im Mai</p>
		<b>Höhenlage</b>
		<p>160m ü.d.M.</p>
		<b>Dämpfung</b>
		<p>tief (Fukamushi, ca. 90 Sek.)</p>
		<b>Beschattung</b>
		<p>7 Tage vor der Ernte, Jikagise-Technik</p>
		<b>Anbau</b>
		<p>Bio-zertifizierter Anbau</p>
		<b>Labortests</b>
		<p>Radioaktivität (Jährlicher Test der Region) und Pflanzenschutzmittel</p>
		<b>Qualität</b>
		<p>98/100 P. (Kategorie Mizudashi); Contest Grade Sencha 96 P., Super Premium</p>
	</div>
{/snippet}
